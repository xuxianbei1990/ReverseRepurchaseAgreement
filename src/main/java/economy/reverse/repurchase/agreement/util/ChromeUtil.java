package economy.reverse.repurchase.agreement.util;

import cn.hutool.system.SystemUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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
        if (SystemUtil.getOsInfo().isLinux()) {
            try {
                driver = (ChromeDriver) new RemoteWebDriver(new URL("http://127.0.0.1:3344"), chromeOptions);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            WebDriverManager.chromedriver().setup();
            ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                    .usingDriverExecutable(new File("C:\\Users\\2250\\.cache\\selenium\\chromedriver\\win32\\105.0.5195.52\\chromedriver.exe")).usingAnyFreePort().build();
            driver = new ChromeDriver(chromeDriverService, chromeOptions);
        }
        driver.manage().window().minimize();
        driver.executeScript("window.open('about:blank', 'tab1');");
        driver.switchTo().window("tab1");
//        driver.quit();
    }


    public static ChromeDriver instance() {
        return driver;
    }

}