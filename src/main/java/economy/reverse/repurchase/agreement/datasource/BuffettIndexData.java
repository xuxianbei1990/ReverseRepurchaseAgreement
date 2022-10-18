package economy.reverse.repurchase.agreement.datasource;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import economy.reverse.repurchase.agreement.dao.BuffettIndexMapper;
import economy.reverse.repurchase.agreement.model.BuffettIndex;
import economy.reverse.repurchase.agreement.model.GrahamIndex;
import economy.reverse.repurchase.agreement.util.ChromeUtil;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 巴菲特指数
 *
 * @author: xuxianbei
 * Date: 2022/10/18
 * Time: 18:02
 * Version:V1.0
 */
@Component
public class BuffettIndexData implements IExecute {

    @Autowired
    private ChromeUtil chromeUtil;

    @Resource
    private BuffettIndexMapper buffettIndexMapper;

    @Override
    public void execute() {
        RemoteWebDriver driver = chromeUtil.instance();
        driver.get("https://legulegu.com/stockdata/marketcap-gdp");
        List<WebElement> list = driver.findElements(By.className("pe"));
        String value = list.get(2).getText().replace("总市值/GDP: ", "").replace("%", "");
        BigDecimal radio = (new BigDecimal(value)).divide(BigDecimal.valueOf(100));
        BuffettIndex grahamIndex = buffettIndexMapper.selectOne(Wrappers.lambdaQuery(BuffettIndex.class)
                .between(BuffettIndex::getCreateDate, TimeThreadSafeUtils.nowMin(), TimeThreadSafeUtils.nowMax()));
        if (grahamIndex == null) {
            grahamIndex = new BuffettIndex();
            grahamIndex.setRatio(radio);
            grahamIndex.setCreateDate(LocalDateTime.now());
            buffettIndexMapper.insert(grahamIndex);
        }
    }
}
