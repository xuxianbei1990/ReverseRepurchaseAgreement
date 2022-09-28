package economy.reverse.repurchase.agreement.strategy;

import economy.reverse.repurchase.agreement.strategy.model.DateOneBigDecimal;
import economy.reverse.repurchase.agreement.util.MyExcelUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2022/9/27
 * Time: 11:37
 * Version:V1.0
 */
public class shangzheng50Stategy extends AbstractExecuteTemplate {

    @Override
    public List<DateOneBigDecimal> parseData() {
        return MyExcelUtil.parseGeLeiEMu();
    }

    @Override
    public void doExecute(List<DateOneBigDecimal> dateOneBigDecimals, Integer times) {
        List<DateOneBigDecimal> low = new ArrayList<>();
        for (DateOneBigDecimal dateOneBigDecimal : dateOneBigDecimals) {
            if (dateOneBigDecimal.getRate().compareTo(BigDecimal.valueOf(3.9153)) > 0) {
                low.add(dateOneBigDecimal);
            }
        }
        System.out.println(BigDecimal.valueOf(low.size()).divide(BigDecimal.valueOf(dateOneBigDecimals.size()), 4, BigDecimal.ROUND_HALF_UP));
    }

    public static void main(String[] args) {
        new shangzheng50Stategy().execute();
    }
}
