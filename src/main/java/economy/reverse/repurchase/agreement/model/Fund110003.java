package economy.reverse.repurchase.agreement.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 易方达上证50指数增强A
 * </p>
 *
 * @author xuxianbei
 * @since 2022-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fund110003")
public class Fund110003 implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 单位净值
     */
    private BigDecimal unit;

    /**
     * 累计净值
     */
    private BigDecimal sum;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;


}
