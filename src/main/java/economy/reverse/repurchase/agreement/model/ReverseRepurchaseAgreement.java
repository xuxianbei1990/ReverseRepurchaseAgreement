package economy.reverse.repurchase.agreement.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *  逆回购
 * </p>
 *
 * @author xuxianbei
 * @since 2022-09-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("reverse_repurchase_agreement")
public class ReverseRepurchaseAgreement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 金额
     */
    private BigDecimal price;

    /**
     * 金额单位
     */
    private String priceUnit;

    /**
     * 增加货币
     */
    private BigDecimal addPrice;

    /**
     * 减少货币
     */
    private BigDecimal subPrice;

    /**
     * 周期
     */
    private Integer period;

    /**
     * 周期单位
     */
    private String periodUnit;

    /**
     * 利率
     */
    private BigDecimal interestRate;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;


}
