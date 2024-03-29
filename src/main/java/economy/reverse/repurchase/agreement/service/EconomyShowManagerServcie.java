package economy.reverse.repurchase.agreement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import economy.reverse.repurchase.agreement.dao.*;
import economy.reverse.repurchase.agreement.model.*;
import economy.reverse.repurchase.agreement.util.MyPageUtil;
import economy.reverse.repurchase.agreement.util.TimeThreadSafeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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

    @Resource
    private GrahamIndexMapper grahamIndexMapper;

    @Resource
    private BuffettIndexMapper buffettIndexMapper;

    /**
     * 增加功能：查3,5,10年出现的概率：汇率。市盈率
     *
     * @return
     */
    public Graph queryGraph() {
        Page<ReverseRepurchaseAgreement> pageRra = new Page<>();
        Page<PriceEarningsRatio> pagePer = MyPageUtil.newPage();
        pagePer.setSize(Long.MAX_VALUE);
        Page<Usdcny> pageUsd = MyPageUtil.newPage();
        Graph graph = new Graph();
        IPage<PriceEarningsRatio> priceEarningsRatios = priceEarningsRatioMapper.selectPage(pagePer, Wrappers.lambdaQuery(PriceEarningsRatio.class).orderByDesc(PriceEarningsRatio::getId));
        IPage<ReverseRepurchaseAgreement> reverseRepurchaseAgreements =
                reverseRepurchaseAgreementMapper.selectPage(pageRra, Wrappers.lambdaQuery(ReverseRepurchaseAgreement.class).orderByDesc(ReverseRepurchaseAgreement::getId));
        IPage<Usdcny> usdcnies = usdcnyMapper.selectPage(pageUsd, Wrappers.lambdaQuery(Usdcny.class).orderByDesc(Usdcny::getId));
        List<MediumtermLendingFacility> mediumtermLendingFacilities = mediumtermLendingFacilityMapper.selectList(Wrappers.lambdaQuery(MediumtermLendingFacility.class).orderByDesc(MediumtermLendingFacility::getId));
        List<Propose> proposes = proposeMapper.selectList(Wrappers.lambdaQuery(Propose.class));
        GrahamIndex grahamIndex = grahamIndexMapper.selectOne(Wrappers.lambdaQuery(GrahamIndex.class)
                .between(GrahamIndex::getCreateDate, TimeThreadSafeUtils.nowMin(), TimeThreadSafeUtils.nowMax()));
        BuffettIndex buffettIndex = buffettIndexMapper.selectOne(Wrappers.lambdaQuery(BuffettIndex.class)
                .between(BuffettIndex::getCreateDate, TimeThreadSafeUtils.nowMin(), TimeThreadSafeUtils.nowMax()));
        priceEarningsRatios.getRecords().sort(Comparator.comparing(PriceEarningsRatio::getId));
        graph.setMediumtermLendingFacilities(mediumtermLendingFacilities);
        graph.setPriceEarningsRatios(priceEarningsRatios.getRecords());
        graph.setReverseRepurchaseAgreements(reverseRepurchaseAgreements);
        usdcnies.getRecords().sort(Comparator.comparing(Usdcny::getId));
        graph.setUsdcnies(usdcnies.getRecords());
        graph.setProposes(proposes);
        graph.setGrahamIndex(grahamIndex);
        graph.setBuffettIndex(buffettIndex);
        return graph;
    }
}
