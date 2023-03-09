package economy.reverse.repurchase.agreement.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.poi.excel.ExcelUtil;
import economy.reverse.repurchase.agreement.model.PriceEarningsRatio;
import economy.reverse.repurchase.agreement.strategy.model.DateOneBigDecimal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author: xuxianbei
 * Date: 2022/9/22
 * Time: 17:00
 * Version:V1.0
 */
public class MyExcelUtil {

    public static void parse(String fileName, Consumer<PriceEarningsRatio> consumer) {
        ExcelUtil.readBySax(MyExcelUtil.class.getResource("/").getFile() + "\\static\\" + fileName,
                0, (sheetIndex, rowIndex, rowCells) -> {
                    if (rowIndex > 1) {
                        PriceEarningsRatio ratio = new PriceEarningsRatio();
                        ratio.setRadioName("沪深300滚动市盈率(TTM)中位数");
                        ratio.setRatio(new BigDecimal(rowCells.get(rowCells.size() - 1).toString()));
                        ratio.setCreateDate(LocalDateTimeUtil.of(DateUtil.parse(rowCells.get(0).toString())));
                        consumer.accept(ratio);
                    }
                });
    }

    public static List<DateOneBigDecimal> parseGeLeiEMu() {
        List<DateOneBigDecimal> list = new ArrayList<>();
        ExcelUtil.readBySax(MyExcelUtil.class.getResource("/").getFile() + "\\static\\格雷厄姆指数-data-2022-09-27.xlsx",
                0, (sheetIndex, rowIndex, rowCells) -> {
                    if (rowIndex > 1) {
                        DateOneBigDecimal ratio = new DateOneBigDecimal();
                        ratio.setDate(LocalDateTimeUtil.of(DateUtil.parse(rowCells.get(0).toString())));
                        ratio.setValue(new BigDecimal(rowCells.get(1).toString()));
                        list.add(ratio);
                    }
                });
        return list;
    }
}
