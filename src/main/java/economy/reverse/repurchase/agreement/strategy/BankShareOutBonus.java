package economy.reverse.repurchase.agreement.strategy;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import economy.reverse.repurchase.agreement.model.Sh600036;
import economy.reverse.repurchase.agreement.util.TxtUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * 分红策略
 *
 * @author: xuxianbei
 * Date: 2022/9/16
 * Time: 15:45
 * Version:V1.0
 */
@Component
public class BankShareOutBonus {

    static class ShareOutBonus {
        LocalDateTime createDate;
        BigDecimal share;
    }

    private static List<ShareOutBonus> shareOutBonuses = new ArrayList<>();

    private static String[] dates =
            /**
             * ICBC
             * 工商银行
             */
//            {"2014-06-13", "2015-06-29",  "2016-07-01",  "2017-07-04", "2018-07-06", "2019-06-27", "2020-06-20", "2021-06-29", "2022-07-05" }
            {"2010-07-08", "2011-06-24", "2012-06-25", "2013-06-21", "2014-07-10", "2015-07-01", "2016-06-30", "2017-06-30", "2018-07-17", "2019-07-10", "2020-07-10", "2021-07-15", "2022-07-08"}
            /**
             * 招商银行
             */
//    {"2015-07-03", "2016-07-13", "2017-06-14", "2018-07-12", "2019-07-12", "2020-07-10", "2021-07-13", "2022-07-15"}
            ;

    private static double[] shares =
//            {2.617, 2.554, 2.333, 	2.343, 2.408, 2.506, 2.628, 2.66, 2.933}
            {2.02, 2.1220, 2.365, 2.68, 3, 3.01, 2.74, 2.78, 2.91, 3.06, 3.2, 3.26, 3.64}
//    {6.7, 6.90, 7.4, 8.4, 9.4, 12, 12.53, 15.22}
            ;

    static {
        for (int i = 0; i < dates.length; i++) {
            ShareOutBonus shareOutBonus = new ShareOutBonus();
            shareOutBonus.share = BigDecimal.valueOf(shares[i]);
            shareOutBonus.createDate = LocalDateTimeUtil.of(DateUtil.parse(dates[i]));
            shareOutBonuses.add(shareOutBonus);
        }

    }


    /**
     * 以建设银行为例。高于6%就分批买入，低于3%分批卖出
     */
    public void execute() {
        List<Sh600036> sh600036s = parseData();
        doExecute(sh600036s, 1);
//        saveData(sh600036s);
    }

    private void doExecute(List<Sh600036> sh600036s, int times) {
        BigDecimal init = BigDecimal.valueOf(100000);
        Stack<BigDecimal> stacks = new Stack<>();
        newValue(init, stacks, times);
        List<BigDecimal> sells = new ArrayList<>();

        List<Sh600036> newSh = sh600036s.stream().filter(t -> t.getCreateDate().compareTo(LocalDateTimeUtil.of(DateUtil.parse("2010-07-08"))) > 0).collect(Collectors.toList());
        BigDecimal count = BigDecimal.ZERO;

        List<Sh600036> low = new ArrayList<>();
        List<Sh600036> height = new ArrayList<>();
        for (Sh600036 sh : newSh) {
            BigDecimal share = sureDate(sh.getCreateDate());
            //分红除以股价
            if (share.divide(sh.getOpen().multiply(BigDecimal.valueOf(10)), 4, RoundingMode.HALF_UP).compareTo(BigDecimal.valueOf(0.07)) >= 0) {
                low.add(sh);
                if (!stacks.isEmpty()) {
                    BigDecimal init1 = stacks.pop();
                    count = init1.divide(sh.getOpen(), 0, RoundingMode.UP);
                    init = init.subtract(init1);
                    System.out.println("买入" + count + "股价：" + sh);
                    sells.add(count);
                }
            }

            if (share.divide(sh.getOpen().multiply(BigDecimal.valueOf(10)), 4, RoundingMode.HALF_UP).compareTo(BigDecimal.valueOf(0.03)) <= 0) {
                height.add(sh);
                if (stacks.isEmpty()) {
                    BigDecimal sellCount = sells.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                    init = sellCount.multiply(sh.getOpen());
                    System.out.println("卖出" + init + "股价：" + sh);
                    count = sellCount;
                    sells.clear();
                    newValue(init, stacks, times);
                }
            }
        }

        BigDecimal sum = init;
        if (sells.size() > 0) {
            count = sells.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        }
        if (count.compareTo(BigDecimal.ZERO) > 0) {
            sum = count.multiply(newSh.get(newSh.size() - 1).getOpen());
        }
        System.out.println("概率" + BigDecimal.valueOf(low.size()).divide(BigDecimal.valueOf(sh600036s.size()), 5, BigDecimal.ROUND_HALF_UP));
        System.out.println("概率" + BigDecimal.valueOf(height.size()).divide(BigDecimal.valueOf(sh600036s.size()), 5, BigDecimal.ROUND_HALF_UP));
        System.out.println(count);
        System.out.println(sum);
        System.out.println(sum.subtract(BigDecimal.valueOf(100000)).divide(BigDecimal.valueOf(100000).multiply(BigDecimal.valueOf(8)), 2, RoundingMode.UP));
    }

    public static void main(String[] args) {
        BankShareOutBonus bankShareOutBonus = new BankShareOutBonus();
        bankShareOutBonus.execute();
    }

    private void newValue(BigDecimal init, Stack<BigDecimal> stacks, Integer times) {
        BigDecimal value1 = init.divide(BigDecimal.valueOf(times), 2, BigDecimal.ROUND_HALF_UP);
        for (int i = 0; i < times; i++) {
            stacks.add(value1);
        }
    }

    private BigDecimal sureDate(LocalDateTime createDate) {
        for (int i = 0; i < shareOutBonuses.size(); i++) {
            if (shareOutBonuses.get(i).createDate.compareTo(createDate) <= 0) {
                if (i + 1 < shareOutBonuses.size() && shareOutBonuses.get(i + 1).createDate.compareTo(createDate) > 0) {
                    return shareOutBonuses.get(i).share;
                }
                if (i + 1 >= shareOutBonuses.size()) {
                    return shareOutBonuses.get(i).share;
                }
            }
        }
        throw new RuntimeException("Data error");
    }

    public List<Sh600036> parseData() {
        return TxtUtils.parseText(new File(this.getClass().getResource("/").getFile() + "\\static\\SH#601939.txt"));
    }


}
