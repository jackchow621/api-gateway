package cn.ghost.service.impl;

import cn.ghost.constants.EnabledEnum;
import cn.ghost.constants.GatewayExceptionEnum;
import cn.ghost.constants.MatchMethodEnum;
import cn.ghost.constants.MatchObjectEnum;
import cn.ghost.exception.GatewayException;
import cn.ghost.listener.event.RuleAddEvent;
import cn.ghost.listener.event.RuleDeleteEvent;
import cn.ghost.mapper.AppInstanceMapper;
import cn.ghost.mapper.AppMapper;
import cn.ghost.mapper.RouteRuleMapper;
import cn.ghost.model.AppRuleDTO;
import cn.ghost.model.bean.App;
import cn.ghost.model.bean.AppInstance;
import cn.ghost.model.bean.RouteRule;
import cn.ghost.model.dto.ChangeStatusDTO;
import cn.ghost.model.dto.RuleDTO;
import cn.ghost.model.vo.RuleVO;
import cn.ghost.service.RuleService;
import cn.ghost.transfer.AppRuleVOTransfer;
import cn.ghost.transfer.RuleVOTransfer;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 17:54
 */
@Service
@Slf4j
public class RuleServiceImpl implements RuleService {

    @Resource
    AppMapper appMapper;
    @Resource
    RouteRuleMapper routeRuleMapper;
    @Resource
    AppInstanceMapper appInstanceMapper;
    @Resource
    ApplicationEventPublisher applicationEventPublisher;

    @Override
    public List<AppRuleDTO> getEnabledRule() {
        QueryWrapper<App> wrapper = Wrappers.query();
        wrapper.lambda().eq(App::getEnabled, EnabledEnum.ENABLE.getCode());
        List<App> apps = appMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(apps)) {
            return Lists.newArrayList();
        }
        List<Integer> appIds = apps.stream().map(App::getId).collect(Collectors.toList());
        Map<Integer, String> nameMap = apps.stream().collect(Collectors.toMap(App::getId, App::getAppName));
        QueryWrapper<RouteRule> query = Wrappers.query();
        query.lambda().in(RouteRule::getAppId, appIds)
                .eq(RouteRule::getEnabled, EnabledEnum.ENABLE.getCode());
        List<RouteRule> routeRules = routeRuleMapper.selectList(query);
        List<AppRuleDTO> appRuleDTOS = AppRuleVOTransfer.INSTANCE.mapToVOList(routeRules);
        appRuleDTOS.forEach(r -> r.setAppName(nameMap.get(r.getAppId())));
        return appRuleDTOS;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(RuleDTO ruleDTO) {
        checkRule(ruleDTO);

        App app = appMapper.selectById(ruleDTO.getAppId());
        RouteRule routeRule = new RouteRule();
        BeanUtils.copyProperties(ruleDTO, routeRule);
        routeRule.setCreatedTime(LocalDateTime.now());
        routeRuleMapper.insert(routeRule);

        if (EnabledEnum.ENABLE.getCode().equals(ruleDTO.getEnabled())) {
            AppRuleDTO appRuleDTO = new AppRuleDTO();
            BeanUtils.copyProperties(routeRule, appRuleDTO);
            appRuleDTO.setAppName(app.getAppName());
            //TODO 往redis中新增规则
            applicationEventPublisher.publishEvent(new RuleAddEvent(this, appRuleDTO));
        }
    }

    private void checkRule(RuleDTO ruleDTO) {
        QueryWrapper<RouteRule> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RouteRule::getName, ruleDTO.getName());
        if (!CollectionUtils.isEmpty(routeRuleMapper.selectList(wrapper))) {
            throw new GatewayException("规则名称不能重复");
        }
        if (MatchObjectEnum.DEFAULT.getCode().equals(ruleDTO.getMatchObject())) {
            ruleDTO.setMatchKey(null);
            ruleDTO.setMatchMethod(null);
            ruleDTO.setMatchRule(null);
        } else {
            if (StringUtils.isEmpty(ruleDTO.getMatchKey()) || ruleDTO.getMatchMethod() == null
                    || StringUtils.isEmpty(ruleDTO.getMatchRule())) {
                throw new GatewayException(GatewayExceptionEnum.PARAM_ERROR);
            }
        }
        // check version
        QueryWrapper<AppInstance> query = Wrappers.query();
        query.lambda().eq(AppInstance::getAppId,ruleDTO.getAppId())
                .eq(AppInstance::getVersion,ruleDTO.getVersion());
        List<AppInstance> list = appInstanceMapper.selectList(query);
        if (CollectionUtils.isEmpty(list)){
            throw new GatewayException("实例版本不存在");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Integer id) {
        RouteRule routeRule = routeRuleMapper.selectById(id);
        if (routeRule == null) {
            throw new GatewayException(GatewayExceptionEnum.PARAM_ERROR);
        }
        AppRuleDTO appRuleDTO = new AppRuleDTO();
        BeanUtils.copyProperties(routeRule, appRuleDTO);
        App app = appMapper.selectById(appRuleDTO.getAppId());
        appRuleDTO.setAppName(app.getAppName());

        routeRuleMapper.deleteById(id);
        //TODO redis中删除规则
        applicationEventPublisher.publishEvent(new RuleDeleteEvent(this, appRuleDTO));
    }

    @Override
    public List<RuleVO> queryList(String appName) {
        Integer appId = null;
        if (!StringUtils.isEmpty(appName)) {
            App app = queryByAppName(appName);
            if (app == null) {
                return Lists.newArrayList();
            }
            appId = app.getId();
        }
        QueryWrapper<RouteRule> query = Wrappers.query();
        query.lambda().eq(Objects.nonNull(appId), RouteRule::getAppId, appId)
                .orderByDesc(RouteRule::getCreatedTime);
        List<RouteRule> rules = routeRuleMapper.selectList(query);
        if (CollectionUtils.isEmpty(rules)) {
            return Lists.newArrayList();
        }
        List<RuleVO> ruleVOS = RuleVOTransfer.INSTANCE.mapToVOList(rules);
        Map<Integer, String> nameMap = getAppNameMap(ruleVOS.stream().map(r -> r.getAppId()).collect(Collectors.toList()));
        ruleVOS.forEach(ruleVO -> {
            ruleVO.setAppName(nameMap.get(ruleVO.getAppId()));
            ruleVO.setMatchStr(buildMatchStr(ruleVO));
        });
        return ruleVOS;
    }

    private String buildMatchStr(RuleVO ruleVO) {
        if (MatchObjectEnum.DEFAULT.getCode().equals(ruleVO.getMatchObject())){
            return ruleVO.getMatchObject();
        }else {
            StringBuilder sb = new StringBuilder();
            sb.append("["+ruleVO.getMatchKey()+"] ");
            sb.append(MatchMethodEnum.getByCode(ruleVO.getMatchMethod()).getDesc());
            sb.append(" ["+ruleVO.getMatchRule()+"]");
            return sb.toString();
        }
    }

    private Map<Integer, String> getAppNameMap(List<Integer> appIdList) {
        QueryWrapper<App> query = Wrappers.query();
        query.lambda().in(App::getId, appIdList);
        List<App> apps = appMapper.selectList(query);
        return apps.stream().collect(Collectors.toMap(App::getId, App::getAppName));
    }

    private App queryByAppName(String appName) {
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(App::getAppName, appName);
        App app = appMapper.selectOne(wrapper);
        return app;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeStatus(ChangeStatusDTO statusDTO) {
        RouteRule routeRule = new RouteRule();
        routeRule.setId(statusDTO.getId());
        routeRule.setEnabled(statusDTO.getEnabled());
        routeRuleMapper.updateById(routeRule);
        AppRuleDTO appRuleDTO = new AppRuleDTO();
        BeanUtils.copyProperties(routeRule, appRuleDTO);
        appRuleDTO.setAppName(statusDTO.getAppName());
        if (EnabledEnum.ENABLE.getCode().equals(statusDTO.getEnabled())) {
            //TODO redis中新增规则
            applicationEventPublisher.publishEvent(new RuleAddEvent(this, appRuleDTO));
        } else {
            //TODO redis中删除规则
            applicationEventPublisher.publishEvent(new RuleDeleteEvent(this, appRuleDTO));
        }
    }
}
