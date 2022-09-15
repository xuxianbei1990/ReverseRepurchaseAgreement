package economy.reverse.repurchase.agreement.event;

import economy.reverse.repurchase.agreement.util.ChromeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @author: xuxianbei
 * Date: 2022/9/6
 * Time: 15:39
 * Version:V1.0
 */
@Component
public class SpringCloseEvent implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    private ChromeUtil chromeUtil;

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        chromeUtil.unInit();
    }

}
