package cn.ghost.common.constants;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 10:15
 */
public class LoadBalanceConstants {
    /**
     * 随机
     */
    public static final String RANDOM = "random";
    /**
     * 轮询
     */
    public static final String ROUND = "round";
    /**
     * 加权轮询
     */
    public static final String WEIGHT_ROUND = "weightRound";
    /**
     * 平滑加权轮询
     */
    public static final String SMOOTH_WEIGHT_ROUND = "smoothWeightRound";
    /**
     * 哈希轮询
     */
    public static final String HASH_ROUND = "hashRound";

}
