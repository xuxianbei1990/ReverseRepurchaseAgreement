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
 * 巴菲特指数
 * </p>
 *
 * @author xuxianbei
 * @since 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("buffett_index")
public class BuffettIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 巴菲特指数
     */
    private BigDecimal ratio;

    /**
     * 创建时间
     */
    private LocalDateTime createDate;


}
