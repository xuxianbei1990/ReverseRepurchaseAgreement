package economy.reverse.repurchase.agreement.strategy.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: xuxianbei
 * Date: 2022/9/27
 * Time: 11:43
 * Version:V1.0
 */
@Data
public class DateOneBigDecimal {

    private LocalDateTime date;

    private BigDecimal value;

}
