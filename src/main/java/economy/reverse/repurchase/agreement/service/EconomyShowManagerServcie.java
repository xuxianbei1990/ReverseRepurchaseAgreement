package economy.reverse.repurchase.agreement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import economy.reverse.repurchase.agreement.dao.MediumtermLendingFacilityMapper;
import economy.reverse.repurchase.agreement.dao.PriceEarningsRatioMapper;
import economy.reverse.repurchase.agreement.dao.ReverseRepurchaseAgreementMapper;
import economy.reverse.repurchase.agreement.dao.UsdcnyMapper;
import economy.reverse.repurchase.agreement.model.*;
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

    @Resource
    private MediumtermLendingFacilityMapper mediumtermLendingFacilityMapper;

    public Graph queryGraph() {
        Page page = new Page<>();
        Graph graph = new Graph();
        List<PriceEarningsRatio> priceEarningsRatios = priceEarningsRatioMapper.selectList(Wrappers.lambdaQuery(PriceEarningsRatio.class));
        IPage<ReverseRepurchaseAgreement> reverseRepurchaseAgreements =
                reverseRepurchaseAgreementMapper.selectPage(page, Wrappers.lambdaQuery(ReverseRepurchaseAgreement.class).orderByDesc(ReverseRepurchaseAgreement::getId));
        List<Usdcny> usdcnies = usdcnyMapper.selectList(Wrappers.lambdaQuery(Usdcny.class));
        List<MediumtermLendingFacility> mediumtermLendingFacilities = mediumtermLendingFacilityMapper.selectList(Wrappers.lambdaQuery(MediumtermLendingFacility.class));
        graph.setMediumtermLendingFacilities(mediumtermLendingFacilities);
        graph.setPriceEarningsRatios(priceEarningsRatios);
        graph.setReverseRepurchaseAgreements(reverseRepurchaseAgreements);
        graph.setUsdcnies(usdcnies);
        return graph;
    }
}
