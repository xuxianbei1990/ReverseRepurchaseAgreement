package economy.reverse.repurchase.agreement.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * @author: xuxianbei
 * Date: 2022/9/7
 * Time: 13:38
 * Version:V1.0
 */
@Data
public class Graph {

    private List<PriceEarningsRatio> priceEarningsRatios;
    private IPage<ReverseRepurchaseAgreement> reverseRepurchaseAgreements;
    private List<Usdcny> usdcnies;
    private List<MediumtermLendingFacility> mediumtermLendingFacilities;
}
