package cn.ghost.common.constants;

import lombok.AllArgsConstructor;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 10:39
 */
@AllArgsConstructor
public enum GatewayPluginEnum {
    AUTH("Auth",1,"鉴权插件"),
    DYNAMIC_ROUTE("DynamicRoute", 2, "动态路由插件");

    private String name;

    private Integer order;

    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
