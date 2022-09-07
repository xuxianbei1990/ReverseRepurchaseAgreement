package economy.reverse.repurchase.agreement.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import economy.reverse.repurchase.agreement.dao.PriceEarningsRatioMapper;
import economy.reverse.repurchase.agreement.dao.ReverseRepurchaseAgreementMapper;
import economy.reverse.repurchase.agreement.dao.UsdcnyMapper;
import economy.reverse.repurchase.agreement.datasource.BankOfChinaData;
import economy.reverse.repurchase.agreement.datasource.PriceEarningsRatioData;
import economy.reverse.repurchase.agreement.datasource.RmbToDollar;
import economy.reverse.repurchase.agreement.model.Graph;
import economy.reverse.repurchase.agreement.model.PriceEarningsRatio;
import economy.reverse.repurchase.agreement.model.ReverseRepurchaseAgreement;
import economy.reverse.repurchase.agreement.model.Usdcny;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2022/9/7
 * Time: 13:41
 * Version:V1.0
 */
@Service
public class EconomyShowManagerServcie {

    @Resource
    private PriceEarningsRatioMapper priceEarningsRatioMapper;

    @Resource
    private ReverseRepurchaseAgreementMapper reverseRepurchaseAgreementMapper;

    @Resource
    private UsdcnyMapper usdcnyMapper;

    public Graph queryGraph() {
        Graph graph = new Graph();
        List<PriceEarningsRatio> priceEarningsRatios = priceEarningsRatioMapper.selectList(Wrappers.lambdaQuery(PriceEarningsRatio.class));
        List<ReverseRepurchaseAgreement> reverseRepurchaseAgreements = reverseRepurchaseAgreementMapper.selectList(Wrappers.lambdaQuery(ReverseRepurchaseAgreement.class));
        List<Usdcny> usdcnies = usdcnyMapper.selectList(Wrappers.lambdaQuery(Usdcny.class));
        graph.setPriceEarningsRatios(priceEarningsRatios);
        graph.setReverseRepurchaseAgreements(reverseRepurchaseAgreements);
        graph.setUsdcnies(usdcnies);
        return graph;
    }
}
