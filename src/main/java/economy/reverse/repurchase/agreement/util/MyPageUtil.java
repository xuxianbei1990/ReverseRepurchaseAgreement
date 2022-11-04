package economy.reverse.repurchase.agreement.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import economy.reverse.repurchase.agreement.model.ReverseRepurchaseAgreement;

/**
 * @author: xuxianbei
 * Date: 2022/11/4
 * Time: 10:56
 * Version:V1.0
 */
public class MyPageUtil {

    public static <T> Page<T> newPage() {
        Page page = new Page<T>();
        page.setSize(30);
        return page;
    }
}
