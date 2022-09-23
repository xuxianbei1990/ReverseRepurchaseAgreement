package economy.reverse.repurchase.agreement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import economy.reverse.repurchase.agreement.dao.*;
import economy.reverse.repurchase.agreement.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
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

    @Resource
    private ProposeMapper proposeMapper;

    public Graph queryGraph() {
        Page page = new Page<ReverseRepurchaseAgreement>();
        Page page2 = new Page<PriceEarningsRatio>();
        Graph graph = new Graph();
        IPage<PriceEarningsRatio> priceEarningsRatios = priceEarningsRatioMapper.selectPage(page2, Wrappers.lambdaQuery(PriceEarningsRatio.class).orderByDesc(PriceEarningsRatio::getId));
        IPage<ReverseRepurchaseAgreement> reverseRepurchaseAgreements =
                reverseRepurchaseAgreementMapper.selectPage(page, Wrappers.lambdaQuery(ReverseRepurchaseAgreement.class).orderByDesc(ReverseRepurchaseAgreement::getId));
        List<Usdcny> usdcnies = usdcnyMapper.selectList(Wrappers.lambdaQuery(Usdcny.class));
        List<MediumtermLendingFacility> mediumtermLendingFacilities = mediumtermLendingFacilityMapper.selectList(Wrappers.lambdaQuery(MediumtermLendingFacility.class));
        List<Propose> proposes = proposeMapper.selectList(Wrappers.lambdaQuery(Propose.class));
        priceEarningsRatios.getRecords().sort(Comparator.comparing(PriceEarningsRatio::getId));
        graph.setMediumtermLendingFacilities(mediumtermLendingFacilities);
        graph.setPriceEarningsRatios(priceEarningsRatios.getRecords());
        graph.setReverseRepurchaseAgreements(reverseRepurchaseAgreements);
        graph.setUsdcnies(usdcnies);
        graph.setProposes(proposes);
        return graph;
    }
}
