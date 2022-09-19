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
@RequestMapping("data/collect")
public class ReverseRepurchaseAgreementController {

    @Autowired
    private ReverseRepurchaseAgreementService reverseRepurchaseAgreementService;

    @GetMapping("china/of/bank")
    public String chinaOfBank() {
        reverseRepurchaseAgreementService.chinaOfBank();
        return "success";
    }

    @GetMapping("usdcny")
    public String usdcny() {
        reverseRepurchaseAgreementService.usdcny();
        return "success";
    }

    @GetMapping("price/earnings/radio")
    public String priceEarningsRadio() {
        reverseRepurchaseAgreementService.priceEarningsRadio();
        return "success";
    }

    @GetMapping("economy/target")
    public String economyTarget() {
        reverseRepurchaseAgreementService.economyTarget();
        return "success";
    }

    @GetMapping("parse/save")
    public String parseSave() {
        reverseRepurchaseAgreementService.parseSave();
        return "success";
    }

    @GetMapping("sample")
    public String sample() {
        reverseRepurchaseAgreementService.sample();
        return "success";
    }
}
