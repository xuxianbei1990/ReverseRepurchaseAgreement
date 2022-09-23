package economy.reverse.repurchase.agreement.strategy;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import economy.reverse.repurchase.agreement.dao.PriceEarningsRatioMapper;
import economy.reverse.repurchase.agreement.dao.Sh600036Mapper;
import economy.reverse.repurchase.agreement.model.PriceEarningsRatio;
import economy.reverse.repurchase.agreement.model.Probability;
import economy.reverse.repurchase.agreement.model.Sh600036;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    public Probability execute(BigDecimal max, BigDecimal min, Integer year) {
        List<Sh600036> sh600036s = loadData();
        return doExecute(sh600036s, 1, max, min, year);
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

    private Probability doExecute(List<Sh600036> sh600036s, int times, BigDecimal value, BigDecimal height, Integer year) {
        BigDecimal init = BigDecimal.valueOf(100000);
        Stack<BigDecimal> stacks = new Stack<>();
        newValue(init, stacks, times);
        List<PriceEarningsRatio> low = new ArrayList<>();
        List<PriceEarningsRatio> height1 = new ArrayList<>();
        List<PriceEarningsRatio> priceEarningsRatios = priceEarningsRatioMapper.selectList(Wrappers.lambdaQuery(PriceEarningsRatio.class)
                .ge(PriceEarningsRatio::getCreateDate, LocalDateTime.now().plusYears(-year)));
        for (int i = 0; i < priceEarningsRatios.size(); i++) {
            if (priceEarningsRatios.get(i).getRatio().compareTo(value) <= 0) {
                low.add(priceEarningsRatios.get(i));

            }

            if (priceEarningsRatios.get(i).getRatio().compareTo(height) >= 0) {
                height1.add(priceEarningsRatios.get(i));
            }
        }
        Probability probability = new Probability();
        probability.setLow("概率->" + BigDecimal.valueOf(low.size()).divide(BigDecimal.valueOf(priceEarningsRatios.size()), 5, BigDecimal.ROUND_HALF_UP));
        probability.setHeight("概率->" + BigDecimal.valueOf(height1.size()).divide(BigDecimal.valueOf(priceEarningsRatios.size()), 5, BigDecimal.ROUND_HALF_UP));
        return probability;
    }
}
