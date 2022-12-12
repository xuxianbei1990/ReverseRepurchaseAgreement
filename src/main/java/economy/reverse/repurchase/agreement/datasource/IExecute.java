package economy.reverse.repurchase.agreement.datasource;

/**
 * @author: xuxianbei
 * Date: 2022/9/6
 * Time: 15:05
 * Version:V1.0
 */
public interface IExecute {

    void execute();

    default boolean judge() {
        return true;
    }
}
