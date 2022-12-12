package economy.reverse.repurchase.agreement.service;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
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
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
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
            if (execute.judge()) {
                execute.execute();
            }
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


    /**
     * blockHandler:那么我猜SentinelResource实现方式是先获取注解的主类，绕后通过扫描主类的下所有方法，通过名字判断，然后注入方法，那么这个是否有
     * 优先顺序？默认参数又是啥呢？blockHandlerClass: 这个默认就是主类，也可以指定类
     *
     * @param id
     * @return
     */
    @SentinelResource(value = "getPropose", blockHandler = "testBlockHandler", blockHandlerClass = ReverseRepurchaseAgreementService.class)
    public Propose getPropose(Integer id) {
        return proposeMapper.selectById(id);
    }

    public static void testBlockHandler() {
        System.out.println("1");
    }


    public Integer upatePropose(Propose propose) {
        return proposeMapper.updateById(propose);
    }
}
