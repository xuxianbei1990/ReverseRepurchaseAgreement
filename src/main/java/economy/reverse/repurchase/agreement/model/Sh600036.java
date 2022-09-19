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
 * 招商银行
 * </p>
 *
 * @author xuxianbei
 * @since 2022-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SH600036")
public class Sh600036 implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 开盘
     */
    private BigDecimal open;

    /**
     * 最高
     */
    private BigDecimal max;

    /**
     * 最低
     */
    private BigDecimal min;

    /**
     * 收盘
     */
    private BigDecimal closing;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;


}
