package economy.reverse.repurchase.agreement.datasource;

import economy.reverse.repurchase.agreement.dao.ReverseRepurchaseAgreementMapper;
import economy.reverse.repurchase.agreement.model.ReverseRepurchaseAgreement;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    public void execute() {
        ReverseRepurchaseAgreement reverseRepurchaseAgreement = getReverseRepurchaseAgreement();
        reverseRepurchaseAgreementMapper.insert(reverseRepurchaseAgreement);
    }

    private ReverseRepurchaseAgreement getReverseRepurchaseAgreement() {
        ChromeDriver driver = ChromeUtil.instance();
        driver.get("http://www.pbc.gov.cn/zhengcehuobisi/125207/125213/125431/index.html");
//        driver.manage().window().maximize();
        List<WebElement> list = driver.findElements(By.tagName("a"));
        Optional<WebElement> optionalWebElement = list.stream().filter(webElement -> judge(webElement.getText())).findFirst();
        WebElement webElement = optionalWebElement.get();
        String url = webElement.getAttribute("href");

        driver.executeScript("window.open('about:blank', 'tab1');");
        driver.switchTo().window("tab1");
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
        driver.quit();
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
