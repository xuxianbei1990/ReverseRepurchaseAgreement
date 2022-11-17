package economy.reverse.repurchase.agreement.service;

import economy.reverse.repurchase.agreement.dao.PriceEarningsRatioMapper;
import economy.reverse.repurchase.agreement.dao.ProposeMapper;
import economy.reverse.repurchase.agreement.dao.Sh600036Mapper;
import economy.reverse.repurchase.agreement.datasource.BankOfChinaData;
import economy.reverse.repurchase.agreement.datasource.IExecute;
import economy.reverse.repurchase.agreement.datasource.PriceEarningsRatioData;
import economy.reverse.repurchase.agreement.datasource.RmbToDollar;
import economy.reverse.repurchase.agreement.model.PriceEarningsRatio;
import economy.reverse.repurchase.agreement.model.Propose;
import economy.reverse.repurchase.agreement.model.ReverseRepurchaseAgreement;
import economy.reverse.repurchase.agreement.model.Sh600036;
import economy.reverse.repurchase.agreement.strategy.BankShareOutBonus;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import economy.reverse.repurchase.agreement.util.MyExcelUtil;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private List<IExecute> executes;

    @Autowired
    private BankOfChinaData bankOfChinaData;

    @Autowired
    private RmbToDollar rmbToDollar;

    @Resource
    private ProposeMapper proposeMapper;

    @Autowired
    private ChromeUtil chromeUtil;

    @Resource
    private PriceEarningsRatioMapper priceEarningsRatioMapper;

    public void chinaOfBank() {
        bankOfChinaData.execute();
    }


    public void usdcny() {
        rmbToDollar.execute();
    }

    public void economyTarget() {
        for (IExecute execute : executes) {
            execute.execute();
        }
    }

    public void priceEarningsRadio() {
        priceEarningsRatioData.execute();
    }

    public void sample() {
        RemoteWebDriver driver = chromeUtil.instance();
        driver.get("https://www.baidu.com/");
    }

    @Transactional(rollbackFor = Exception.class)
    public void parseSave() {
        MyExcelUtil.parse("沪深300市盈率.xlsx", (priceEarningsRatio) -> priceEarningsRatioMapper.insert(priceEarningsRatio));
    }

    public Propose getPropose(Integer id) {
        return proposeMapper.selectById(id);
    }

    public Integer upatePropose(Propose propose) {
        return proposeMapper.updateById(propose);
    }
}
