package economy.reverse.repurchase.agreement.util;

import cn.hutool.system.SystemUtil;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2022/9/6
 * Time: 14:18
 * Version:V1.0
 */
@Slf4j
@Component
public class ChromeUtil implements InitializingBean {

    private RemoteWebDriver driver;

    public RemoteWebDriver instance() {
        return driver;
    }

//    public static void main(String[] args) throws MalformedURLException {
//        FirefoxOptions firefoxOptions = new FirefoxOptions();
//        firefoxOptions.setPageLoadTimeout(Duration.ofSeconds(20));
//        firefoxOptions.setImplicitWaitTimeout(Duration.ofSeconds(20));
//
//        ChromeOptions chromeOptions = new ChromeOptions();
//        List<String> list1 = new ArrayList<>();
//        chromeOptions.addArguments(list1);
//        list1.add("--no-sandbox");
//        list1.add("--disable-extensions");
//        list1.add("--disable-dev-shm-usage");
//        //# 谷歌文档提到需要加上这个属性来规避bug
//        list1.add("--disable-gpu");
//        //'window-size=1920x3000')  # 指定浏览器分辨率
//        //隐藏滚动条, 应对一些特殊页面
//        list1.add("--hide-scrollbars");
//        // 不加载图片, 提升速度
//        list1.add("blink-settings=imagesEnabled=false");
//        //# 浏览器不提供可视化页面. linux下如果系统不支持可视化不加这条会启动失败
//        list1.add("--headless");
////        chromeOptions.setExperimentalOption("excludeSwitches", "enable-automation");
//        chromeOptions.setExperimentalOption("useAutomationExtension", false);
//        chromeOptions.setPageLoadTimeout(Duration.ofSeconds(20));
//        chromeOptions.setImplicitWaitTimeout(Duration.ofSeconds(20));
//        RemoteWebDriver driver = null;
//        try {
//            ChromeDriver chromeDriver = new ChromeDriver();
//            driver = new RemoteWebDriver(new URL("http://43.143.47.137:3344/wd/hub"), chromeOptions);
//            driver.get("http://www.pbc.gov.cn/zhengcehuobisi/125207/125213/125431/index.html");
//            //http://www.pbc.gov.cn/zhengcehuobisi/125207/125213/125431/index.html
//            //https://legulegu.com/stockdata/hs300-ttm-lyr
//
//            List<WebElement> list = driver.findElements(By.tagName("a"));
//            System.out.println("======" + list.stream().map(WebElement::getText).collect(Collectors.toList()));
//        } finally {
//            if (driver != null) {
//                driver.close();
//                driver.quit();
//            }
//        }
//    }

    public void init() {
        ChromeOptions chromeOptions = new ChromeOptions();
//        if (SystemUtil.getOsInfo().isLinux()) {
//            try {
//                //因为每个docker都有自己ip，无法直接使用localhost
//                driver = new RemoteWebDriver(new URL("http://43.143.47.137:3344/wd/hub"), chromeOptions);
//            } catch (MalformedURLException e) {
//                log.error("驱动失败", e);
//            }
//        } else {
        WebDriverManager.chromedriver().setup();
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

        String driverPath = "chromedriver.exe";
        if (SystemUtil.getOsInfo().isLinux()) {
            driverPath = "chromedriver";
        }

        ChromeDriverService chromeDriverService = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(driverPath)).usingAnyFreePort().build();
        driver = new ChromeDriver(chromeDriverService, chromeOptions);
        driver.executeScript("window.open('about:blank', 'tab1');");
        driver.switchTo().window("tab1");
        driver.manage().window().minimize();
//        }
        log.info("创建成功");
    }

    public void unInit() {
        driver.quit();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}