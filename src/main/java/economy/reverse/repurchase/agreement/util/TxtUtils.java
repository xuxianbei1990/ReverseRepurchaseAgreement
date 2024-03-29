package economy.reverse.repurchase.agreement.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import economy.reverse.repurchase.agreement.model.PriceEarningsRatio;
import economy.reverse.repurchase.agreement.model.Sh600036;
import economy.reverse.repurchase.agreement.strategy.model.DateOneBigDecimal;
import netscape.javascript.JSObject;
import org.apache.ibatis.io.Resources;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Name
 *
 * @author xxb
 * Date 2022/9/16
 * VersionV1.0
 * @description
 */
public class TxtUtils {

    public static void main(String[] args) {
        parseJsonToPer("");
    }

    public static List<PriceEarningsRatio> parseBftJsonToPer(String filePath) {
        String json = FileUtil.readString(TxtUtils.class.getResource("/").getFile() + "\\static\\巴菲特指数.txt", Charset.forName("gb2312"));
        JSONObject jsonArray = JSONUtil.parseObj(json);
        List<JSONObject> jsonObjects =  jsonArray.getBeanList("data", JSONObject.class);
        List<PriceEarningsRatio> list = jsonObjects.stream().map(value -> {
            PriceEarningsRatio ratio = new PriceEarningsRatio();
            ratio.setCreateDate(LocalDateTimeUtil.of(value.getLong("date")));
            ratio.setRadioName("总市值比GDP");
            ratio.setRatio(value.getBigDecimal("marketCap").divide(value.getBigDecimal("gdp"), 4, BigDecimal.ROUND_HALF_UP));
            return ratio;
        }).collect(Collectors.toList());
        return list;
    }


    public static List<PriceEarningsRatio> parseJsonToPer(String filePath) {
        String json = FileUtil.readString(TxtUtils.class.getResource("/").getFile() + "\\static\\创业50.txt", Charset.forName("gb2312"));
        JSONObject jsonArray = JSONUtil.parseObj(json);
        List<JSONObject> jsonObjects =  jsonArray.getBeanList("data", JSONObject.class);
        List<PriceEarningsRatio> list = jsonObjects.stream().map(value -> {
            PriceEarningsRatio ratio = new PriceEarningsRatio();
            ratio.setCreateDate(LocalDateTimeUtil.of(value.getLong("date")));
            ratio.setRadioName("创业50滚动市盈率(TTM)中位数");
            ratio.setRatio(value.getBigDecimal("middleTtmPe"));
            return ratio;
        }).collect(Collectors.toList());
        return list;
    }

    public static List<Sh600036> parseText(File fie) {
        List<String> list = FileUtil.readLines(fie, Charset.forName("gb2312"));
        return list.stream().skip(2).map(value -> {
            if (value.equals("数据来源:通达信")) {
                return null;
            }
            String[] values = value.split("\t");
            Sh600036 sh600036 = new Sh600036();
            sh600036.setCreateDate(LocalDateTimeUtil.of(DateUtil.parse(values[0])));
            sh600036.setOpen(new BigDecimal(values[1]));
            sh600036.setMax(new BigDecimal(values[2]));
            sh600036.setMin(new BigDecimal(values[3]));
            sh600036.setClosing(new BigDecimal(values[4]));
            return sh600036;
        }).filter(t -> t != null).collect(Collectors.toList());

    }

    public static List<DateOneBigDecimal> parseText(File fie, Class<DateOneBigDecimal> zlass) {
        List<String> list = FileUtil.readLines(fie, Charset.forName("gb2312"));
        return list.stream().skip(2).map(value -> {
            if (value.equals("数据来源:通达信")) {
                return null;
            }
            String[] values = value.split("\t");
            DateOneBigDecimal sh600036 = new DateOneBigDecimal();
            sh600036.setDate(LocalDateTimeUtil.of(DateUtil.parse(values[0])));
            sh600036.setValue(new BigDecimal(values[4]));
            return sh600036;
        }).filter(t -> t != null).collect(Collectors.toList());
    }

}
