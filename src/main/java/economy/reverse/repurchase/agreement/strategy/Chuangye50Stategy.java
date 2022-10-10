package economy.reverse.repurchase.agreement.strategy;

import economy.reverse.repurchase.agreement.model.PriceEarningsRatio;
import economy.reverse.repurchase.agreement.strategy.model.DateOneBigDecimal;
import economy.reverse.repurchase.agreement.util.TxtUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: xuxianbei
 * Date: 2022/10/10
 * Time: 14:32
 * Version:V1.0
 */
public class Chuangye50Stategy extends AbstractExecuteTemplate {

    @Override
    public List<DateOneBigDecimal> parseData() {
        List<PriceEarningsRatio> priceEarningsRatios = TxtUtils.parseJsonToPer("");
        return priceEarningsRatios.stream().map(priceEarningsRatio -> {
            DateOneBigDecimal oneBigDecimal = new DateOneBigDecimal();
            oneBigDecimal.setDate(priceEarningsRatio.getCreateDate());
            oneBigDecimal.setRate(priceEarningsRatio.getRatio());
            return oneBigDecimal;
        }).collect(Collectors.toList());
    }

    @Override
    public void doExecute(List<DateOneBigDecimal> dateOneBigDecimals, Integer times) {
        List<DateOneBigDecimal> low = new ArrayList<>();
        List<DateOneBigDecimal> height = new ArrayList<>();
        for (int i = 0; i < dateOneBigDecimals.size(); i++) {
            if (dateOneBigDecimals.get(i).getRate().compareTo(BigDecimal.valueOf(37)) < 0) {
                low.add(dateOneBigDecimals.get(i));
//                if (init.compareTo(BigDecimal.ZERO) > 0) {
//                    Fund110003 fund110003 = list.get(i);
//                    count = init.divide(fund110003.getUnit(), 0, BigDecimal.ROUND_HALF_UP);
//                    System.out.println("买入" + count + "股价：" + fund110003);
//                    init = BigDecimal.ZERO;
//                }
            }
            if (dateOneBigDecimals.get(i).getRate().compareTo(BigDecimal.valueOf(100)) > 0) {
                height.add(dateOneBigDecimals.get(i));
//                if (init.compareTo(BigDecimal.ZERO) == 0){
//                    Fund110003 fund110003 = list.get(i);
//                    init = count.multiply(fund110003.getUnit());
//                    System.out.println("卖出" + init + "股价：" + fund110003);
//                    count = BigDecimal.ZERO;
//                }
            }
        }
        System.out.println(BigDecimal.valueOf(low.size()).divide(BigDecimal.valueOf(dateOneBigDecimals.size()), 4, BigDecimal.ROUND_HALF_UP));
        System.out.println(BigDecimal.valueOf(height.size()).divide(BigDecimal.valueOf(dateOneBigDecimals.size()), 4, BigDecimal.ROUND_HALF_UP));
    }

    public static void main(String[] args) {
        Chuangye50Stategy chuangye50Stategy = new Chuangye50Stategy();
        chuangye50Stategy.execute();
    }
}
