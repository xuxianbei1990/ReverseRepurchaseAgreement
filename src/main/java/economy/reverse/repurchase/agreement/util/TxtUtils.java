package economy.reverse.repurchase.agreement.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import economy.reverse.repurchase.agreement.model.Sh600036;
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
}
