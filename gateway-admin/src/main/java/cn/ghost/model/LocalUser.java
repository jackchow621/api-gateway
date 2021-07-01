package cn.ghost.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program api-gateway
 * @description:
 * @author: zoulinjun
 * @create: 2021/07/01 16:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalUser {
    private Integer userId;

    private String name;

}
