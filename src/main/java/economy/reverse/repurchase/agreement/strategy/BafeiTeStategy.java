package economy.reverse.repurchase.agreement.strategy;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import economy.reverse.repurchase.agreement.dao.BuffettIndexMapper;
import economy.reverse.repurchase.agreement.model.BuffettIndex;
import economy.reverse.repurchase.agreement.model.GrahamIndex;
import economy.reverse.repurchase.agreement.model.PriceEarningsRatio;
import economy.reverse.repurchase.agreement.strategy.model.DateOneBigDecimal;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
import economy.reverse.repurchase.agreement.util.TxtUtils;
import economy.reverse.repurchase.agreement.util.mysql.MySqlBuffettIndex;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 巴菲特指数
 *
 * @author: xuxianbei
 * Date: 2022/10/11
 * Time: 14:06
 * Version:V1.0
 */
@Component
public class BafeiTeStategy extends AbstractExecuteTemplate {

    @Resource
    private BuffettIndexMapper buffettIndexMapper;

    @Override
    public List<DateOneBigDecimal> parseData() {
        List<PriceEarningsRatio> list = TxtUtils.parseBftJsonToPer("");

        return list.stream().filter(t -> t.getCreateDate().compareTo(LocalDateTime.of(2017, 1, 1, 0, 0, 0)) > 0).map(priceEarningsRatio -> {
            DateOneBigDecimal oneBigDecimal = new DateOneBigDecimal();
            oneBigDecimal.setDate(priceEarningsRatio.getCreateDate());
            oneBigDecimal.setRate(priceEarningsRatio.getRatio());
            return oneBigDecimal;
        }).collect(Collectors.toList());
    }

    public List<DateOneBigDecimal> LoadDataFromDB() {
        List<BuffettIndex> list = buffettIndexMapper.selectList(Wrappers.lambdaQuery(BuffettIndex.class));
        return list.stream().map(priceEarningsRatio -> {
            DateOneBigDecimal oneBigDecimal = new DateOneBigDecimal();
            oneBigDecimal.setDate(priceEarningsRatio.getCreateDate());
            oneBigDecimal.setRate(priceEarningsRatio.getRatio());
            return oneBigDecimal;
        }).collect(Collectors.toList());
    }

    public String execute(BigDecimal low, BigDecimal high) {
        return doExecuteByFixYear(LoadDataFromDB(), low, high);
    }

    @Override
    public void doExecute(List<DateOneBigDecimal> dateOneBigDecimals, Integer times) {
        MySqlBuffettIndex mySqlBuffettIndex = new MySqlBuffettIndex();
        mySqlBuffettIndex.executeCreateAndUpdate(dateOneBigDecimals.stream().map(dateOneBigDecimal -> {
            GrahamIndex grahamIndex = new GrahamIndex();
            grahamIndex.setRatio(dateOneBigDecimal.getRate());
            grahamIndex.setCreateDate(dateOneBigDecimal.getDate());
            return grahamIndex;
        }).collect(Collectors.toList()));

//        List<DateOneBigDecimal> low = new ArrayList<>();
//        List<DateOneBigDecimal> height = new ArrayList<>();
//        for (int i = 0; i < dateOneBigDecimals.size(); i++) {
//            if (dateOneBigDecimals.get(i).getRate().compareTo(BigDecimal.valueOf(0.7)) < 0) {
//                low.add(dateOneBigDecimals.get(i));
//            }
//            if (dateOneBigDecimals.get(i).getRate().compareTo(BigDecimal.valueOf(0.92)) > 0) {
//                height.add(dateOneBigDecimals.get(i));
//            }
//        }
//        System.out.println(BigDecimal.valueOf(low.size()).divide(BigDecimal.valueOf(dateOneBigDecimals.size()), 4, BigDecimal.ROUND_HALF_UP));
//        System.out.println(BigDecimal.valueOf(height.size()).divide(BigDecimal.valueOf(dateOneBigDecimals.size()), 4, BigDecimal.ROUND_HALF_UP));
    }

    public static void main(String[] args) {
        BafeiTeStategy chuangye50Stategy = new BafeiTeStategy();
        chuangye50Stategy.execute();
    }
}
