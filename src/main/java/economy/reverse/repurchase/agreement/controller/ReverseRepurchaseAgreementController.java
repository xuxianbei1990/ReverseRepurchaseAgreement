package economy.reverse.repurchase.agreement.controller;

import economy.reverse.repurchase.agreement.model.Propose;
import economy.reverse.repurchase.agreement.service.ReverseRepurchaseAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: xuxianbei
 * Date: 2022/9/6
 * Time: 9:50
 * Version:V1.0
 */
@CrossOrigin
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

    /**
     * 获取建议：
     * @param id
     * @return
     */
    @GetMapping("get/propose")
    public Propose getPropose(Integer id) {
        return reverseRepurchaseAgreementService.getPropose(id);
    }

    /**
     * 更新建议：
     * @param propose
     * @return
     */
    @PostMapping("upate/propose")
    public Integer upatePropose(@RequestBody Propose propose) {
        return reverseRepurchaseAgreementService.upatePropose(propose);
    }


}
