package cn.ghost.server.handler;

import cn.ghost.common.constants.OperationTypeEnum;
import cn.ghost.common.model.dto.AppRuleDTO;
import cn.ghost.common.model.dto.RouteRuleOperationDTO;
import cn.ghost.server.cache.RouteRuleCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 16:10
 */
public class MessageHandler {

    private Gson gson = new GsonBuilder().create();

    public void handler(String message) {
        RouteRuleOperationDTO operationDTO = gson.fromJson(message, RouteRuleOperationDTO.class);
        if (CollectionUtils.isEmpty(operationDTO.getRuleList())) {
            return;
        }
        Map<String, List<AppRuleDTO>> map = operationDTO.getRuleList()
                .stream().collect(Collectors.groupingBy(AppRuleDTO::getAppName));
        if (OperationTypeEnum.INSERT.getCode().equals(operationDTO.getOperationType())
                || OperationTypeEnum.UPDATE.getCode().equals(operationDTO.getOperationType())) {
            RouteRuleCache.add(map);
        } else if (OperationTypeEnum.DELETE.getCode().equals(operationDTO.getOperationType())) {
            RouteRuleCache.remove(map);
        }
    }
}
