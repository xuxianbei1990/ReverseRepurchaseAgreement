package economy.reverse.repurchase.agreement.strategy;

import economy.reverse.repurchase.agreement.model.Sh600036;
import economy.reverse.repurchase.agreement.strategy.model.DateOneBigDecimal;

import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2022/9/27
 * Time: 11:48
 * Version:V1.0
 */
public abstract class AbstractExecuteTemplate implements Execute{

    public void execute(){
        List<DateOneBigDecimal> sh600036s = parseData();
        doExecute(sh600036s, 1);
    }
}
