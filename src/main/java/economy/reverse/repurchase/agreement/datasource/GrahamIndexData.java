package economy.reverse.repurchase.agreement.datasource;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import economy.reverse.repurchase.agreement.dao.GrahamIndexMapper;
import economy.reverse.repurchase.agreement.model.GrahamIndex;
import economy.reverse.repurchase.agreement.model.ReverseRepurchaseAgreement;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 格雷厄姆指数 = 股票市盈率倒数 / 十年期国债收益率
 *
 * @author: xuxianbei
 * Date: 2022/10/14
 * Time: 11:34
 * Version:V1.0
 */
@Component
public class GrahamIndexData implements IExecute {

    @Autowired
    private ChromeUtil chromeUtil;

    @Resource
    private GrahamIndexMapper grahamIndexMapper;

    private BigDecimal geLeiEMuIndexNum() {
        RemoteWebDriver driver = chromeUtil.instance();
        //https://wallstreetcn.com/markets/CN10YR.OTC
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
    }

    @Override
    public void execute() {
        GrahamIndex grahamIndex = grahamIndexMapper.selectOne(Wrappers.lambdaQuery(GrahamIndex.class)
                .between(GrahamIndex::getCreateDate, TimeThreadSafeUtils.nowMin(), TimeThreadSafeUtils.nowMax()));
        if (grahamIndex == null) {
            BigDecimal radio = geLeiEMuIndexNum();
            grahamIndex = new GrahamIndex();
            grahamIndex.setRatio(radio);
            grahamIndex.setCreateDate(LocalDateTime.now());
            grahamIndexMapper.insert(grahamIndex);
        }
    }
}
