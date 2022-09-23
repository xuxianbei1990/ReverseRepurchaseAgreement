package economy.reverse.repurchase.agreement.model;

import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2022/9/23
 * Time: 13:43
 * Version:V1.0
 */
@Data
public class Probability {

    /**
     * 低于
     */
    private String low;

    /**
     * 高于
     */
    private String height;
}
