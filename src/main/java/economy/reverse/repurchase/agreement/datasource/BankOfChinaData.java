package economy.reverse.repurchase.agreement.datasource;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import economy.reverse.repurchase.agreement.dao.MediumtermLendingFacilityMapper;
import economy.reverse.repurchase.agreement.dao.ReverseRepurchaseAgreementMapper;
import economy.reverse.repurchase.agreement.model.MediumtermLendingFacility;
import economy.reverse.repurchase.agreement.model.ReverseRepurchaseAgreement;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Resource
    private MediumtermLendingFacilityMapper mediumtermLendingFacilityMapper;

    @Autowired
    private ChromeUtil chromeUtil;

    @Override
    public void execute() {
        Context context = getReverseRepurchaseAgreement();
        executeMlf(context);
        List<ReverseRepurchaseAgreement> list = reverseRepurchaseAgreementMapper.selectList(Wrappers.lambdaQuery(ReverseRepurchaseAgreement.class)
                .between(ReverseRepurchaseAgreement::getCreateDate, TimeThreadSafeUtils.nowMin(), TimeThreadSafeUtils.nowMax()));
        if (CollectionUtils.isEmpty(list)) {
            ReverseRepurchaseAgreement old = reverseRepurchaseAgreementMapper.selectOne(Wrappers.lambdaQuery(ReverseRepurchaseAgreement.class)
                    .between(ReverseRepurchaseAgreement::getCreateDate,
                            TimeThreadSafeUtils.nowMin().plusDays(-context.rra.getPeriod()),
                            TimeThreadSafeUtils.nowMax().plusDays(-context.rra.getPeriod())));
            if (old != null) {
                BigDecimal sub = context.rra.getPrice().subtract(old.getPrice());
                if (sub.compareTo(BigDecimal.ZERO) < 0) {
                    sub = sub.abs();
                    context.rra.setSubPrice(sub);
                } else {
                    context.rra.setAddPrice(sub);
                }
            }
            reverseRepurchaseAgreementMapper.insert(context.rra);
        }
    }

    private void executeMlf(Context context) {
        if (context.mlf == null) {
            return;
        }
        List<MediumtermLendingFacility> list = mediumtermLendingFacilityMapper.selectList(Wrappers.lambdaQuery(MediumtermLendingFacility.class)
                .between(MediumtermLendingFacility::getCreateDate, TimeThreadSafeUtils.nowMin(), TimeThreadSafeUtils.nowMax()));
        if (CollectionUtils.isEmpty(list)) {
            MediumtermLendingFacility old = mediumtermLendingFacilityMapper.selectOne(Wrappers.lambdaQuery(MediumtermLendingFacility.class)
                    .between(MediumtermLendingFacility::getCreateDate,
                            TimeThreadSafeUtils.nowMin().plusYears(-context.mlf.getPeriod()),
                            TimeThreadSafeUtils.nowMax().plusYears(-context.mlf.getPeriod())));
            if (old != null) {
                BigDecimal sub = context.mlf.getPrice().subtract(old.getPrice());
                if (sub.compareTo(BigDecimal.ZERO) < 0) {
                    sub = sub.abs();
                    context.mlf.setSubPrice(sub);
                } else {
                    context.mlf.setAddPrice(sub);
                }
            }
            mediumtermLendingFacilityMapper.insert(context.mlf);
        }
    }

    static class Context {
        ReverseRepurchaseAgreement rra;
        MediumtermLendingFacility mlf;
    }

    private Context getReverseRepurchaseAgreement() {
        RemoteWebDriver driver = chromeUtil.instance();
        driver.get("http://www.pbc.gov.cn/zhengcehuobisi/125207/125213/125431/index.html");
//        driver.manage().window().maximize();
        List<WebElement> list = driver.findElements(By.tagName("a"));
        Optional<WebElement> optionalWebElement = list.stream().filter(webElement -> judge(webElement.getText())).findFirst();
        WebElement webElement = optionalWebElement.get();
        String url = webElement.getAttribute("href");


        driver.get(url);
        List<WebElement> listP = driver.findElements(By.tagName("p"));
        ReverseRepurchaseAgreement rra = null;
        MediumtermLendingFacility mlf = null;

        for (WebElement element : listP) {
            if (element.getText().equals("MLF")) {
                mlf = new MediumtermLendingFacility();
            }
            if (element.getText().endsWith("年") && (mlf != null)) {
                mlf.setPeriod(Integer.valueOf(element.findElement(By.tagName("span")).getText()));
            }
            if (element.getText().endsWith("亿元") && (mlf != null)) {
                mlf.setPrice(new BigDecimal(element.findElement(By.tagName("span")).getText()));
            }
            if (element.getText().endsWith("%") && (mlf != null)) {
                mlf.setInterestRate(new BigDecimal(element.getText().replace("%", "")));
            }
        }

        for (WebElement element : listP) {
            if (element.getText().equals("逆回购操作情况")) {
                rra = new ReverseRepurchaseAgreement();
            }
            if (element.getText().endsWith("天")) {
                rra.setPeriod(Integer.valueOf(element.findElement(By.tagName("span")).getText()));
            }
            if (element.getText().endsWith("亿元")) {
                rra.setPrice(new BigDecimal(element.findElement(By.tagName("span")).getText()));
            }
            if (element.getText().endsWith("%")) {
                rra.setInterestRate(new BigDecimal(element.getText().replace("%", "")));
            }
        }
        Context context = new Context();
        context.mlf = mlf;
        context.rra = rra;
        return context;
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
