package economy.reverse.repurchase.agreement.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 中期借贷便利
 * </p>
 *
 * @author xuxianbei
 * @since 2022-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("MediumTerm_Lending_Facility")
public class MediumtermLendingFacility implements Serializable {

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
     * 减少货币
     */
    private BigDecimal subPrice;

    /**
     * 增加货币
     */
    private BigDecimal addPrice;

    /**
     * 金额单位
     */
    private String priceUnit;

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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime createDate;


}
