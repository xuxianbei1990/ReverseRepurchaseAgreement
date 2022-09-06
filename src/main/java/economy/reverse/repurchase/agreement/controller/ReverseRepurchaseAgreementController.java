package economy.reverse.repurchase.agreement.controller;

import economy.reverse.repurchase.agreement.service.ReverseRepurchaseAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: xuxianbei
 * Date: 2022/9/6
 * Time: 9:50
 * Version:V1.0
 */
@RestController
@RequestMapping("reverse/repurchase/agreement")
public class ReverseRepurchaseAgreementController {

    @Autowired
    private ReverseRepurchaseAgreementService reverseRepurchaseAgreementService;

    @GetMapping("china/of/bank")
    public String chinaOfBank(){
        reverseRepurchaseAgreementService.chinaOfBank();
        return "success";
    }
}
