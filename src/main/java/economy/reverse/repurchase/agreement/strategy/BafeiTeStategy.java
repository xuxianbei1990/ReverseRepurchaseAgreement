package economy.reverse.repurchase.agreement.strategy;

import cn.hutool.core.date.LocalDateTimeUtil;
import economy.reverse.repurchase.agreement.model.PriceEarningsRatio;
import economy.reverse.repurchase.agreement.strategy.model.DateOneBigDecimal;
import economy.reverse.repurchase.agreement.util.TxtUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: xuxianbei
 * Date: 2022/10/11
 * Time: 14:06
 * Version:V1.0
 */
public class BafeiTeStategy extends AbstractExecuteTemplate {
    @Override
    public List<DateOneBigDecimal> parseData() {
        List<PriceEarningsRatio> list = TxtUtils.parseBftJsonToPer("");

        return list.stream().filter(t -> t.getCreateDate().compareTo(LocalDateTime.of(2015, 4, 4, 0, 0, 0)) > 0).map(priceEarningsRatio -> {
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
            if (dateOneBigDecimals.get(i).getRate().compareTo(BigDecimal.valueOf(0.72)) < 0) {
                low.add(dateOneBigDecimals.get(i));
//                if (init.compareTo(BigDecimal.ZERO) > 0) {
//                    Fund110003 fund110003 = list.get(i);
//                    count = init.divide(fund110003.getUnit(), 0, BigDecimal.ROUND_HALF_UP);
//                    System.out.println("买入" + count + "股价：" + fund110003);
//                    init = BigDecimal.ZERO;
//                }
            }
            if (dateOneBigDecimals.get(i).getRate().compareTo(BigDecimal.valueOf(0.8)) > 0) {
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
        BafeiTeStategy chuangye50Stategy = new BafeiTeStategy();
        chuangye50Stategy.execute();
    }
}
