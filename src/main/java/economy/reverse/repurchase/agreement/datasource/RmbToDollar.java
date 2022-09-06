package economy.reverse.repurchase.agreement.datasource;

import economy.reverse.repurchase.agreement.dao.ReverseRepurchaseAgreementMapper;
import economy.reverse.repurchase.agreement.dao.UsdcnyMapper;
import economy.reverse.repurchase.agreement.model.Usdcny;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
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
@Component
public class RmbToDollar implements IExecute {


    @Resource
    private UsdcnyMapper usdcnyMapper;

    private static Usdcny getRmbToDollar() {
        ChromeDriver driver = ChromeUtil.instance();
        driver.get(" https://finance.sina.com.cn/money/forex/hq/USDCNY.shtml");
        List<WebElement> list = driver.findElements(By.tagName("li"));

        List<String> value = list.stream().map(WebElement::getText).collect(Collectors.toList());
        Usdcny usdcny = new Usdcny();
        for (String s : value) {
            if (s.startsWith("昨收\n")){
                usdcny.setExchangeRate(new BigDecimal(s.replace("昨收\n", "")));
                break;
            }
        }
        return usdcny;
    }

    @Override
    public void execute() {
        usdcnyMapper.insert(getRmbToDollar());
    }
}
