package economy.reverse.repurchase.agreement.service;

import economy.reverse.repurchase.agreement.datasource.BankOfChinaData;
import economy.reverse.repurchase.agreement.datasource.PriceEarningsRatioData;
import economy.reverse.repurchase.agreement.datasource.RmbToDollar;
import economy.reverse.repurchase.agreement.model.ReverseRepurchaseAgreement;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: xuxianbei
 * Date: 2022/9/6
 * Time: 10:23
 * Version:V1.0
 */
@Service
public class ReverseRepurchaseAgreementService {


    @Autowired
    private PriceEarningsRatioData priceEarningsRatioData;

    @Autowired
    private BankOfChinaData bankOfChinaData;

    @Autowired
    private RmbToDollar rmbToDollar;

    public void chinaOfBank() {
        bankOfChinaData.execute();
    }


    public void usdcny() {
        rmbToDollar.execute();
    }

    public void economyTarget() {
        bankOfChinaData.execute();
        rmbToDollar.execute();
        priceEarningsRatioData.execute();
    }

    public void priceEarningsRadio() {
        priceEarningsRatioData.execute();
    }

    public void sample() {
        ChromeDriver driver = ChromeUtil.instance();
        driver.get("https://www.baidu.com/");
    }
}
