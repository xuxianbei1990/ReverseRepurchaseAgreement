package economy.reverse.repurchase.agreement.service;

import economy.reverse.repurchase.agreement.dao.Sh600036Mapper;
import economy.reverse.repurchase.agreement.datasource.BankOfChinaData;
import economy.reverse.repurchase.agreement.datasource.PriceEarningsRatioData;
import economy.reverse.repurchase.agreement.datasource.RmbToDollar;
import economy.reverse.repurchase.agreement.model.ReverseRepurchaseAgreement;
import economy.reverse.repurchase.agreement.model.Sh600036;
import economy.reverse.repurchase.agreement.strategy.BankShareOutBonus;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Autowired
    private ChromeUtil chromeUtil;

    @Autowired
    private BankShareOutBonus bankShareOutBonus;

    @Resource
    private Sh600036Mapper sh600036Mapper;

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
        RemoteWebDriver driver = chromeUtil.instance();
        driver.get("https://www.baidu.com/");
    }

    public void parseSave() {
        List<Sh600036> sh600036s = bankShareOutBonus.parseData();
        sh600036s.forEach(sh600036 -> sh600036Mapper.insert(sh600036));
    }
}
