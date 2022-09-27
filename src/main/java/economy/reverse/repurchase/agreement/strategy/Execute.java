package economy.reverse.repurchase.agreement.strategy;

import economy.reverse.repurchase.agreement.model.Sh600036;
import economy.reverse.repurchase.agreement.strategy.model.DateOneBigDecimal;

import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2022/9/27
 * Time: 11:46
 * Version:V1.0
 */
public interface Execute {

    List<DateOneBigDecimal> parseData();

    void execute();

    void doExecute(List<DateOneBigDecimal> dateOneBigDecimals, Integer times);
}
