package economy.reverse.repurchase.agreement.strategy;

import economy.reverse.repurchase.agreement.model.Sh600036;
import economy.reverse.repurchase.agreement.strategy.model.DateOneBigDecimal;
import economy.reverse.repurchase.agreement.util.TxtUtils;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author: xuxianbei
 * Date: 2023/2/28
 * Time: 15:00
 * Version:V1.0
 * 波动算法：
 * 从周一开始计算到周五结束算波动率，
 */
public class UndulateStategy {

    public void execute() {
        List<DateOneBigDecimal> sh600036s = parseData();
        System.out.println(doExecute(sh600036s, LocalDateTime.of(2021, 1, 1, 0, 0),
                LocalDateTime.of(2023, 1, 1, 0, 0), BigDecimal.valueOf(5),
                (oneBigDecimal) ->
                        oneBigDecimal.getDate().getDayOfWeek().getValue() == 5
        ));

    }

    private List<DateOneBigDecimal> parseData() {
        return TxtUtils.parseText(new File(this.getClass().getResource("/").getFile() + "\\static\\SH#601939.txt"), DateOneBigDecimal.class);
    }

    private Integer doExecute(List<DateOneBigDecimal> dates, LocalDateTime begin, LocalDateTime end, BigDecimal rang
            , Predicate<DateOneBigDecimal> supplier) {
        List<DateOneBigDecimal> dealWithDate = dates.stream().filter(t -> t.getDate().compareTo(begin) > 0 && t.getDate().compareTo(end) <= 0).collect(Collectors.toList());
        Integer result = 0;
        DateOneBigDecimal one = null;
        DateOneBigDecimal last = null;
        for (DateOneBigDecimal oneBigDecimal : dealWithDate) {
            if (supplier.test(oneBigDecimal)) {
                if (one != null && last != null) {
                    if ((one.getValue().divide(last.getValue(), 2, BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.ONE.subtract(rang.divide(BigDecimal.valueOf(100)))) <= 0)
                            || (last.getValue().divide(one.getValue(), 2, BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.ONE.add(rang.divide(BigDecimal.valueOf(100)))) >= 0)) {
                        result++;
                    }
                }
                one = oneBigDecimal;
                last = null;
            } else {
                last = oneBigDecimal;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        UndulateStategy undulateStategy = new UndulateStategy();
        undulateStategy.execute();
    }
}
