package economy.reverse.repurchase.agreement.strategy;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import economy.reverse.repurchase.agreement.dao.PriceEarningsRatioMapper;
import economy.reverse.repurchase.agreement.dao.Sh600036Mapper;
import economy.reverse.repurchase.agreement.model.PriceEarningsRatio;
import economy.reverse.repurchase.agreement.model.Sh600036;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author: xuxianbei
 * Date: 2022/9/22
 * Time: 18:17
 * Version:V1.0
 */
@Component
public class PriceEarningsRatioStategy {

    @Resource
    private PriceEarningsRatioMapper priceEarningsRatioMapper;

    @Resource
    private Sh600036Mapper sh600036Mapper;

    public void execute(BigDecimal max, BigDecimal min) {
        List<Sh600036> sh600036s = loadData();
        doExecute(sh600036s, 1, max, min);

    }

    private List<Sh600036> loadData() {
        return sh600036Mapper.selectList(Wrappers.lambdaQuery(Sh600036.class));
    }

    private void newValue(BigDecimal init, Stack<BigDecimal> stacks, Integer times) {
        BigDecimal value1 = init.divide(BigDecimal.valueOf(times), 2, BigDecimal.ROUND_HALF_UP);
        for (int i = 0; i < times; i++) {
            stacks.add(value1);
        }
    }

    private void doExecute(List<Sh600036> sh600036s, int times, BigDecimal value, BigDecimal min) {
        BigDecimal init = BigDecimal.valueOf(100000);
        Stack<BigDecimal> stacks = new Stack<>();
        newValue(init, stacks, times);
        List<BigDecimal> sells = new ArrayList<>();

        List<Sh600036> newSh = sh600036s;
        BigDecimal count = BigDecimal.ZERO;

        List<Sh600036> low = new ArrayList<>();
        List<PriceEarningsRatio> priceEarningsRatios = priceEarningsRatioMapper.selectList(Wrappers.lambdaQuery(PriceEarningsRatio.class)
                .between(PriceEarningsRatio::getCreateDate, sh600036s.get(0).getCreateDate(), sh600036s.get(sh600036s.size() - 1).getCreateDate()));
        for (int i = 0; i < newSh.size(); i++) {
            //分红除以股价
            if (priceEarningsRatios.get(i).getRatio().compareTo(value) <= 0) {
                low.add(newSh.get(i));
                if (!stacks.isEmpty()) {
                    BigDecimal init1 = stacks.pop();
                    count = init1.divide(newSh.get(i).getOpen(), 0, RoundingMode.UP);
                    init = init.subtract(init1);
                    System.out.println("买入" + count + "股价：" + newSh.get(i));
                    sells.add(count);
                }
            }

            if (priceEarningsRatios.get(i).getRatio().compareTo(min) >= 0) {
                if (stacks.isEmpty()) {
                    BigDecimal sellCount = sells.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                    init = sellCount.multiply(newSh.get(i).getOpen());
                    System.out.println("卖出" + init + "股价：" + newSh.get(i));
                    count = sellCount;
                    sells.clear();
                    newValue(init, stacks, times);
                }
            }
        }

        BigDecimal sum = init;
        if (sells.size() > 0) {
            count = sells.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        }
        if (count.compareTo(BigDecimal.ZERO) > 0) {
            sum = count.multiply(newSh.get(newSh.size() - 1).getOpen());
        }
        System.out.println("概率" + BigDecimal.valueOf(low.size()).divide(BigDecimal.valueOf(sh600036s.size()), 5, BigDecimal.ROUND_HALF_UP));
        System.out.println(count);
        System.out.println(sum);
        System.out.println(sum.subtract(BigDecimal.valueOf(100000)).divide(BigDecimal.valueOf(100000).multiply(BigDecimal.valueOf(8)), 2, RoundingMode.UP));
    }
}
