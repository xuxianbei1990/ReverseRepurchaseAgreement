package economy.reverse.repurchase.agreement.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2022/9/6
 * Time: 14:18
 * Version:V1.0
 */
public class ChromeUtil {
    static ChromeDriver driver;

    static {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        List<String> list1 = new ArrayList<>();
        chromeOptions.addArguments(list1);
        list1.add("--no-sandbox");
        list1.add("--disable-dev-shm-usage");
        //# 谷歌文档提到需要加上这个属性来规避bug
        list1.add("--disable-gpu");
        //'window-size=1920x3000')  # 指定浏览器分辨率
        //隐藏滚动条, 应对一些特殊页面
        list1.add("--hide-scrollbars");
        // 不加载图片, 提升速度
        list1.add("blink-settings=imagesEnabled=false");
        //# 浏览器不提供可视化页面. linux下如果系统不支持可视化不加这条会启动失败
        list1.add("--headless");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().window().minimize();
//        driver.quit();
    }


    public static ChromeDriver instance() {
        return driver;
    }
}