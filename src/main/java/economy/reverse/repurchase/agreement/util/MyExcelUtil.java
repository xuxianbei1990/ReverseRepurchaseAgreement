package economy.reverse.repurchase.agreement.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import economy.reverse.repurchase.agreement.model.PriceEarningsRatio;

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

    public static void parse(String filePath, Consumer<PriceEarningsRatio> consumer) {
        ExcelUtil.readBySax(MyExcelUtil.class.getResource("/").getFile() + "\\static\\沪深300市盈率.xlsx",
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
}
