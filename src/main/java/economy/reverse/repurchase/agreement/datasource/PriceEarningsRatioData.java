package economy.reverse.repurchase.agreement.datasource;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import economy.reverse.repurchase.agreement.dao.PriceEarningsRatioMapper;
import economy.reverse.repurchase.agreement.model.PriceEarningsRatio;
import economy.reverse.repurchase.agreement.model.ReverseRepurchaseAgreement;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: xuxianbei
 * Date: 2022/9/6
 * Time: 16:45
 * Version:V1.0
 */
@Component
public class PriceEarningsRatioData implements IExecute {

    @Resource
    private PriceEarningsRatioMapper priceEarningsRatioMapper;

    @Override
    public void execute() {
        PriceEarningsRatio priceEarningsRatio = getPriceEarningsRatio();
        List<PriceEarningsRatio> list = priceEarningsRatioMapper.selectList(Wrappers.lambdaQuery(PriceEarningsRatio.class)
                .between(PriceEarningsRatio::getCreateDate, TimeThreadSafeUtils.nowMin(), TimeThreadSafeUtils.nowMax()));
        if (CollectionUtils.isEmpty(list)) {
            priceEarningsRatioMapper.insert(priceEarningsRatio);
        }
    }

    private static PriceEarningsRatio getPriceEarningsRatio() {
        RemoteWebDriver driver = ChromeUtil.instance();
        driver.get("https://legulegu.com/stockdata/hs300-ttm-lyr");
//        driver.manage().window().maximize();
        List<WebElement> list = driver.findElements(By.tagName("td"));
        List<String> strings = list.stream().map(WebElement::getText).collect(Collectors.toList());
        PriceEarningsRatio priceEarningsRatio = new PriceEarningsRatio();
        for (int i = 0; i < strings.size(); i++) {
            if (strings.get(i).equals("沪深300滚动市盈率(TTM)中位数")) {
                priceEarningsRatio.setRadioName("沪深300滚动市盈率(TTM)中位数");
                priceEarningsRatio.setRatio(new BigDecimal(strings.get(i + 1)));
                break;
            }
        }
        return priceEarningsRatio;
    }
}
