package economy.reverse.repurchase.agreement.controller;

import economy.reverse.repurchase.agreement.model.Graph;
import economy.reverse.repurchase.agreement.service.EconomyShowManagerServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("graph")
    public Graph queryGraph() {
        return economyShowManagerServcie.queryGraph();
    }
}
