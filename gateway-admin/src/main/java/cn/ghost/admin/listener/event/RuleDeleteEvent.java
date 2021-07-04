package cn.ghost.admin.listener.event;

import cn.ghost.common.model.dto.AppRuleDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 15:35
 */
@Getter
public class RuleDeleteEvent extends ApplicationEvent {
    private AppRuleDTO appRuleDTO;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public RuleDeleteEvent(Object source, AppRuleDTO appRuleDTO) {
        super(source);
        this.appRuleDTO = appRuleDTO;
    }
}
