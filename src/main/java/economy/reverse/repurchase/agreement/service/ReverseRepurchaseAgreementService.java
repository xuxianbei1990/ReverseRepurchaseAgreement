package economy.reverse.repurchase.agreement.service;

import economy.reverse.repurchase.agreement.datasource.BankOfChinaData;
import economy.reverse.repurchase.agreement.model.ReverseRepurchaseAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: xuxianbei
 * Date: 2022/9/6
 * Time: 10:23
 * Version:V1.0
 */
@Service
public class ReverseRepurchaseAgreementService {


    @Autowired
    private BankOfChinaData bankOfChinaData;

    public void chinaOfBank() {
        bankOfChinaData.execute();
    }
}
