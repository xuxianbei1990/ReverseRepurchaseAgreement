package economy.reverse.repurchase.agreement.schedule;

import economy.reverse.repurchase.agreement.service.ReverseRepurchaseAgreementService;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * 模拟人
 *
 * @author: xuxianbei
 * Date: 2022/9/7
 * Time: 10:48
 * Version:V1.0
 */
@Component
public class ImitateHuman implements InitializingBean {

    @Autowired
    private AsyncTaskExecutor executor;

    @Autowired
    private ReverseRepurchaseAgreementService reverseRepurchaseAgreementService;

    @Override
    public void afterPropertiesSet() throws Exception {
        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000 * 60);
                    LocalDateTime now = TimeThreadSafeUtils.now();
                    if (now.getHour() > 9 && now.getHour() < 10) {
                        Random random = new Random();
                        Thread.sleep(random.nextInt(10) * 6000);
                        reverseRepurchaseAgreementService.economyTarget();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
