package economy.reverse.repurchase.agreement.fund;

import economy.reverse.repurchase.agreement.model.Fund110003;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
import economy.reverse.repurchase.agreement.util.mysql.MySql;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: xuxianbei
 * Date: 2022/9/28
 * Time: 18:26
 * Version:V1.0
 */
public class FundData {

    //http://fund.eastmoney.com/f10/F10DataApi.aspx

    /**
     * http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code=110003&page=108&sdate=2005-01-01&edate=2022-09-28&per=40
     */

    private String baseUrl = "http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code=%s&page=%s&sdate=%s&edate=%s&per=40";

    public void executeSave() {
        ChromeUtil chromeUtil = new ChromeUtil();
        chromeUtil.init();
        RemoteWebDriver driver = chromeUtil.instance();
        for (int i = 109; i > 0; i--) {
            driver.get(String.format("http://fund.eastmoney.com/f10/F10DataApi.aspx?type=lsjz&code=110003&page=%d&sdate=2005-01-01&edate=2022-09-28&per=40", i));
//        driver.manage().window().maximize();
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
            mySql.execute(fund110003s);
        }
        chromeUtil.unInit();
    }

    public List<Fund110003> select() {
        MySql mySql = new MySql();
        return mySql.executeSelect();
    }

    public static void main(String[] args) {
        new FundData().executeSave();
    }
}
