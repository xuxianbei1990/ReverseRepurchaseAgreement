package economy.reverse.repurchase.agreement.strategy;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import economy.reverse.repurchase.agreement.model.Fund110003;
import economy.reverse.repurchase.agreement.strategy.model.DateOneBigDecimal;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import economy.reverse.repurchase.agreement.util.MyExcelUtil;
import economy.reverse.repurchase.agreement.util.mysql.MySql;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 格雷厄姆指数
 * <p>
 * 格雷厄姆指数 = 股票盈利收益率 / 无风险利率（即十年期国债收益率）
 *
 * @author: xuxianbei
 * Date: 2022/9/27
 * Time: 11:37
 * Version:V1.0
 * https://api-ddc-wscn.awtmt.com/market/kline?prod_code=CN10YR.OTC&tick_count=256&period_type=2592000&adjust_price_type=forward&fields=tick_at,open_px,close_px,high_px,low_px,turnover_volume,turnover_value,average_px,px_change,px_change_rate,avg_px,ma2
 */
public class Shangzheng50Stategy extends AbstractExecuteTemplate {

    @Override
    public List<DateOneBigDecimal> parseData() {
        return MyExcelUtil.parseGeLeiEMu();
    }

    @Override
    public void doExecute(List<DateOneBigDecimal> dateOneBigDecimals, Integer times) {
        List<DateOneBigDecimal> low = new ArrayList<>();
        List<DateOneBigDecimal> height = new ArrayList<>();
        BigDecimal init = BigDecimal.valueOf(100000);
        MySql mySql = new MySql();
        List<Fund110003> list = mySql.executeSelect("161613");
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
                if (init.compareTo(BigDecimal.ZERO) == 0) {
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

    private BigDecimal geLeiEMuIndexNum() {
        ChromeUtil chromeUtil = new ChromeUtil();
        chromeUtil.init();
        try {
            RemoteWebDriver driver = chromeUtil.instance();
            driver.get("https://api-ddc-wscn.awtmt.com/market/kline?prod_code=CN10YR.OTC&tick_count=1&period_type=2592000&adjust_price_type=forward&fields=tick_at,open_px,close_px,high_px,low_px,turnover_volume,turnover_value,average_px,px_change,px_change_rate,avg_px,ma2");
            String value = driver.findElement(By.tagName("pre")).getText();
            JSONObject jsonObject = JSONUtil.parseObj(value);
            BigDecimal year10 = jsonObject.getJSONObject("data").getJSONObject("candle").getJSONObject("CN10YR.OTC").getJSONArray("lines")
                    .getJSONArray(0).getBigDecimal(3);
            year10 = year10.divide(BigDecimal.valueOf(100));

            driver.get("https://legulegu.com/stockdata/sz50-ttm-lyr");
            List<WebElement> list = driver.findElements(By.tagName("tr"));
            BigDecimal sz50 = null;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getText().startsWith("上证50滚动市盈率(TTM) ")) {
                    sz50 = new BigDecimal(list.get(i).getText().replace("上证50滚动市盈率(TTM) ", ""));
                    break;
                }
            }
            return BigDecimal.valueOf(1).divide(sz50, 4, BigDecimal.ROUND_HALF_UP).divide(year10, 4, BigDecimal.ROUND_HALF_UP);
        } finally {
            chromeUtil.unInit();
        }
    }


    public static void main(String[] args) {
        new Shangzheng50Stategy().geLeiEMuIndexNum();
    }
}
