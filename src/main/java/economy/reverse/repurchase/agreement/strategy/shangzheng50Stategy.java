package economy.reverse.repurchase.agreement.strategy;

import economy.reverse.repurchase.agreement.model.Fund110003;
import economy.reverse.repurchase.agreement.strategy.model.DateOneBigDecimal;
import economy.reverse.repurchase.agreement.util.MyExcelUtil;
import economy.reverse.repurchase.agreement.util.mysql.MySql;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        //如果出现这个信号，就买入，那么历史上表现情况如何？
        List<DateOneBigDecimal> low = new ArrayList<>();
        List<DateOneBigDecimal> height = new ArrayList<>();
        BigDecimal init = BigDecimal.valueOf(100000);
        MySql mySql = new MySql();
        List<Fund110003> list = mySql.executeSelect();
        BigDecimal count = BigDecimal.ZERO;
        for (int i = 0; i < dateOneBigDecimals.size(); i++) {
            if (dateOneBigDecimals.get(i).getRate().compareTo(BigDecimal.valueOf(3.6)) > 0) {
                low.add(dateOneBigDecimals.get(i));
                if (init.compareTo(BigDecimal.ZERO) > 0) {
                    Fund110003 fund110003 = list.get(i);
                    count = init.divide(fund110003.getUnit(), 0, BigDecimal.ROUND_HALF_UP);
                    System.out.println("买入" + count + "股价：" + fund110003);
                    init = BigDecimal.ZERO;
                }
            }
            if (dateOneBigDecimals.get(i).getRate().compareTo(BigDecimal.valueOf(2)) < 0) {
                height.add(dateOneBigDecimals.get(i));
                if (init.compareTo(BigDecimal.ZERO) == 0){
                    Fund110003 fund110003 = list.get(i);
                    init = count.multiply(fund110003.getUnit());
                    System.out.println("卖出" + init + "股价：" + fund110003);
                    count = BigDecimal.ZERO;
                }

            }
        }
        System.out.println(init.subtract(BigDecimal.valueOf(100000)).divide(BigDecimal.valueOf(100000).multiply(BigDecimal.valueOf(10)), 2, RoundingMode.UP));
        System.out.println(BigDecimal.valueOf(low.size()).divide(BigDecimal.valueOf(dateOneBigDecimals.size()), 4, BigDecimal.ROUND_HALF_UP));
        System.out.println(BigDecimal.valueOf(height.size()).divide(BigDecimal.valueOf(dateOneBigDecimals.size()), 4, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * 出现这个信号，历史的概率是多少，越低越值得投入
     *
     * @param dateOneBigDecimals
     */
    private void probability(List<DateOneBigDecimal> dateOneBigDecimals) {
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
