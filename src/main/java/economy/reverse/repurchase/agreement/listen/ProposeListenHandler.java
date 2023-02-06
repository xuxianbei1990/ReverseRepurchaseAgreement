package economy.reverse.repurchase.agreement.listen;

import economy.reverse.repurchase.agreement.model.Propose;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;

/**
 *
 * canal 也可以看做一个mybatis的拦截器，好处就是这个拦截器可以拦截自己和其他的数据库的表
 * @author: xuxianbei
 * Date: 2023/1/11
 * Time: 11:05
 * Version:V1.0
 */
//@CanalTable("propose")
//@Component
public class ProposeListenHandler implements EntryHandler<Propose> {

    @Override
    public void insert(Propose propose) {

    }

    @Override
    public void update(Propose before, Propose after) {

    }

    @Override
    public void delete(Propose propose) {

    }
}
