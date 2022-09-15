package economy.reverse.repurchase.agreement.datasource;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import economy.reverse.repurchase.agreement.dao.ReverseRepurchaseAgreementMapper;
import economy.reverse.repurchase.agreement.model.ReverseRepurchaseAgreement;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: xuxianbei
 * Date: 2022/9/5
 * Time: 16:34
 * Version:V1.0
 */
@Component
public class BankOfChinaData implements IExecute {

    @Resource
    private ReverseRepurchaseAgreementMapper reverseRepurchaseAgreementMapper;

    @Autowired
    private ChromeUtil chromeUtil;

    @Override
    public void execute() {
        ReverseRepurchaseAgreement reverseRepurchaseAgreement = getReverseRepurchaseAgreement();
        List<ReverseRepurchaseAgreement> list = reverseRepurchaseAgreementMapper.selectList(Wrappers.lambdaQuery(ReverseRepurchaseAgreement.class)
                .between(ReverseRepurchaseAgreement::getCreateDate, TimeThreadSafeUtils.nowMin(), TimeThreadSafeUtils.nowMax()));
        if (CollectionUtils.isEmpty(list)) {
            ReverseRepurchaseAgreement old = reverseRepurchaseAgreementMapper.selectOne(Wrappers.lambdaQuery(ReverseRepurchaseAgreement.class)
                    .between(ReverseRepurchaseAgreement::getCreateDate,
                            TimeThreadSafeUtils.nowMin().plusDays(-reverseRepurchaseAgreement.getPeriod()),
                            TimeThreadSafeUtils.nowMax().plusDays(-reverseRepurchaseAgreement.getPeriod())));
            if (old != null) {
                BigDecimal sub = reverseRepurchaseAgreement.getPrice().subtract(old.getPrice());
                if (sub.compareTo(BigDecimal.ZERO) < 0){
                    sub = sub.abs();
                    reverseRepurchaseAgreement.setSubPrice(sub);
                } else {
                    reverseRepurchaseAgreement.setAddPrice(sub);
                }
            }
            reverseRepurchaseAgreementMapper.insert(reverseRepurchaseAgreement);
        }
    }

    private ReverseRepurchaseAgreement getReverseRepurchaseAgreement() {
        RemoteWebDriver driver = chromeUtil.instance();
        driver.get("http://www.pbc.gov.cn/zhengcehuobisi/125207/125213/125431/index.html");
//        driver.manage().window().maximize();
        List<WebElement> list = driver.findElements(By.tagName("a"));
        Optional<WebElement> optionalWebElement = list.stream().filter(webElement -> judge(webElement.getText())).findFirst();
        WebElement webElement = optionalWebElement.get();
        String url = webElement.getAttribute("href");


        driver.get(url);
        List<WebElement> listP = driver.findElements(By.tagName("p"));
        ReverseRepurchaseAgreement reverseRepurchaseAgreement = new ReverseRepurchaseAgreement();
        listP.forEach(webElement1 -> {
            if (webElement1.getText().endsWith("天")) {
                reverseRepurchaseAgreement.setPeriod(Integer.valueOf(webElement1.findElement(By.tagName("span")).getText()));
            }
            if (webElement1.getText().endsWith("亿元")) {
                reverseRepurchaseAgreement.setPrice(new BigDecimal(webElement1.findElement(By.tagName("span")).getText()));
            }
            if (webElement1.getText().endsWith("%")) {
                reverseRepurchaseAgreement.setInterestRate(new BigDecimal(webElement1.getText().replace("%", "")));
            }
        });
        return reverseRepurchaseAgreement;
    }

    private static boolean judge(String text) {
        if (text.length() > "公开市场业务交易公告 [2022]第".length()) {
            if (text.startsWith("公开市场业务交易公告 [2022]第")) {
                return true;
            }
        }
        return false;
    }



}
