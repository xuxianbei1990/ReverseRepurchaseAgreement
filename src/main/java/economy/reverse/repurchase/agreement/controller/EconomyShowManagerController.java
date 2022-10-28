package economy.reverse.repurchase.agreement.controller;

import economy.reverse.repurchase.agreement.dao.GrahamIndexMapper;
import economy.reverse.repurchase.agreement.model.Graph;
import economy.reverse.repurchase.agreement.model.Probability;
import economy.reverse.repurchase.agreement.service.EconomyShowManagerServcie;
import economy.reverse.repurchase.agreement.strategy.BafeiTeStategy;
import economy.reverse.repurchase.agreement.strategy.PriceEarningsRatioStategy;
import economy.reverse.repurchase.agreement.strategy.Shangzheng50Stategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author: xuxianbei
 * Date: 2022/9/7
 * Time: 13:36
 * Version:V1.0
 */
@CrossOrigin
@RestController
@RequestMapping("data/show")
public class EconomyShowManagerController {

    @Autowired
    private EconomyShowManagerServcie economyShowManagerServcie;

    @Autowired
    private PriceEarningsRatioStategy priceEarningsRatioStategy;

    @Autowired
    private BafeiTeStategy bafeiTeStategy;

    @Autowired
    private Shangzheng50Stategy shangzheng50Stategy;

    @GetMapping("graph")
    public Graph queryGraph() {
        return economyShowManagerServcie.queryGraph();
    }

    @GetMapping("strategy")
    public Probability strategy(BigDecimal low, BigDecimal height, Integer year) {
        return priceEarningsRatioStategy.execute(low, height, year);
    }

    @GetMapping("strategy/buffett")
    public String strategyBuffett(BigDecimal low, BigDecimal high) {
        return bafeiTeStategy.execute(low, high);
    }

    @GetMapping("strategy/graham")
    public String strategyGraham(BigDecimal low, BigDecimal high) {
        return shangzheng50Stategy.execute(low, high);
    }
}
