package economy.reverse.repurchase.agreement.fund;

import economy.reverse.repurchase.agreement.model.Fund110003;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
import economy.reverse.repurchase.agreement.util.mysql.MySql;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author: xuxianbei
 * Date: 2022/9/28
 * Time: 18:26
 * Version:V1.0
 */
@Component
public class FundData {


    /**
     * http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code=110003&page=108&sdate=2005-01-01&edate=2022-09-28&per=40
     */

    private String baseUrl = "http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code=%s&page=%s&sdate=%s&edate=%s&per=40";

    public void executeSave(String code, String codeName, String beginDate, String endDate) {
        ChromeUtil chromeUtil = new ChromeUtil();
        chromeUtil.init();
        RemoteWebDriver driver = chromeUtil.instance();
        driver.get(String.format(baseUrl,
                code, new Random().nextInt(5), beginDate, endDate));
        Integer pages = Integer.valueOf(driver.getPageSource().split(",")[2].split(":")[1]);
        for (int i = pages; i > 0; i--) {
            driver.get(String.format(baseUrl, code, i, beginDate, endDate));
            List<WebElement> list = driver.findElements(By.tagName("tr"));
            list = list.stream().skip(1).collect(Collectors.toList());
            List<Fund110003> fund110003s = new ArrayList();
            list.stream().forEach(webElement -> {
                List<WebElement> sons = webElement.findElements(By.tagName("td"));
                Fund110003 fund110003 = new Fund110003();
                fund110003.setCreateDate(TimeThreadSafeUtils.stringToLocalDateTime(sons.get(0).getText()));
                fund110003.setUnit(new BigDecimal(sons.get(1).getText()));
                fund110003.setSum(new BigDecimal(sons.get(2).getText()));
                fund110003s.add(fund110003);
            });
            MySql mySql = new MySql();
            fund110003s.sort(Comparator.comparing(Fund110003::getCreateDate));
            mySql.executeCreateAndUpdate(fund110003s, code, codeName);
        }
        chromeUtil.unInit();
    }

    public List<Fund110003> select(String code) {
        MySql mySql = new MySql();
        return mySql.executeSelect(code);
    }

    public static void main(String[] args) {
        new FundData().executeSave("161613", "融通创业板指数A/B", "2012-09-27", "2022-9-27");
    }
}
