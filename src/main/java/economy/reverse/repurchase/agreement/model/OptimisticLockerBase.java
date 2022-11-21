package economy.reverse.repurchase.agreement.model;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

/**
 * @author: xuxianbei
 * Date: 2022/11/21
 * Time: 9:48
 * Version:V1.0
 */
@Data
public class OptimisticLockerBase {

    @Version
    private Integer version;
}
