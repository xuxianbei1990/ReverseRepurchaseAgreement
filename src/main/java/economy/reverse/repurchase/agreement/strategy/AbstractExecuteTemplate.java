package economy.reverse.repurchase.agreement.strategy;

import economy.reverse.repurchase.agreement.strategy.model.DateOneBigDecimal;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public String doExecuteByFixYear(List<DateOneBigDecimal> dateOneBigDecimals, BigDecimal lowIndex, BigDecimal highIndex) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("3年");
        calc(getYearCollect(dateOneBigDecimals, -3), lowIndex, highIndex, stringBuilder);
        stringBuilder.append(System.lineSeparator() + "5年");
        calc(getYearCollect(dateOneBigDecimals, -5), lowIndex, highIndex, stringBuilder);
        stringBuilder.append(System.lineSeparator() + "10年");
        calc(getYearCollect(dateOneBigDecimals, -10), lowIndex, highIndex, stringBuilder);
        stringBuilder.append(System.lineSeparator() + "20年");
        calc(getYearCollect(dateOneBigDecimals, -20), lowIndex, highIndex, stringBuilder);
        return stringBuilder.toString();
    }

    private List<DateOneBigDecimal> getYearCollect(List<DateOneBigDecimal> dateOneBigDecimals, Integer year) {
        return dateOneBigDecimals.stream().filter(dateOneBigDecimal ->
                dateOneBigDecimal.getDate().compareTo(TimeThreadSafeUtils.nowMin().plusYears(year)) > 0).collect(Collectors.toList());
    }

    private void calc(List<DateOneBigDecimal> dateOneBigDecimals, BigDecimal lowIndex, BigDecimal highIndex, StringBuilder stringBuilder) {
        List<DateOneBigDecimal> low = new ArrayList<>();
        List<DateOneBigDecimal> height = new ArrayList<>();
        for (int i = 0; i < dateOneBigDecimals.size(); i++) {
            if (dateOneBigDecimals.get(i).getValue().compareTo(lowIndex) < 0) {
                low.add(dateOneBigDecimals.get(i));
            }
            if (dateOneBigDecimals.get(i).getValue().compareTo(highIndex) > 0) {
                height.add(dateOneBigDecimals.get(i));
            }
        }

        stringBuilder.append(" 低于").append(lowIndex.toString()).append(" 概率:")
                .append(BigDecimal.valueOf(low.size()).divide(BigDecimal.valueOf(dateOneBigDecimals.size()), 4, BigDecimal.ROUND_HALF_UP));
        stringBuilder.append(" 高于").append(highIndex.toString()).append(" 概率：")
                .append(BigDecimal.valueOf(height.size()).divide(BigDecimal.valueOf(dateOneBigDecimals.size()), 4, BigDecimal.ROUND_HALF_UP));
    }
}
