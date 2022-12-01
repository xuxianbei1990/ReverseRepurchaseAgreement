package economy.reverse.repurchase.agreement.datasource;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import economy.reverse.repurchase.agreement.dao.ReverseRepurchaseAgreementMapper;
import economy.reverse.repurchase.agreement.dao.UsdcnyMapper;
import economy.reverse.repurchase.agreement.model.ReverseRepurchaseAgreement;
import economy.reverse.repurchase.agreement.model.Usdcny;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: xuxianbei
 * Date: 2022/9/6
 * Time: 14:16
 * Version:V1.0
 */
@Slf4j
@Component
public class RmbToDollar implements IExecute {

    @Autowired
    private ChromeUtil chromeUtil;

    @Resource
    private UsdcnyMapper usdcnyMapper;

    private Usdcny getRmbToDollar() {
        RemoteWebDriver driver = chromeUtil.instance();
        driver.get(" https://finance.sina.com.cn/money/forex/hq/USDCNY.shtml");
        List<WebElement> list = driver.findElements(By.tagName("li"));
        for (WebElement webElement : list) {
            try {
                String s = webElement.getText();
                if (s.startsWith("昨收\n")) {
                    Usdcny usdcny = new Usdcny();
                    usdcny.setExchangeRate(new BigDecimal(s.replace("昨收\n", "")));
                    return usdcny;
                }
            } catch (Exception e){
                log.error("错误", e);
            }

        }
        throw new RuntimeException("获取数据失败");
    }

    @Override
    public void execute() {
        List<Usdcny> list = usdcnyMapper.selectList(Wrappers.lambdaQuery(Usdcny.class)
                .between(Usdcny::getCreateDate, TimeThreadSafeUtils.nowMin(), TimeThreadSafeUtils.nowMax()));
        if (CollectionUtils.isEmpty(list)) {
            usdcnyMapper.insert(getRmbToDollar());
        }
    }
}
