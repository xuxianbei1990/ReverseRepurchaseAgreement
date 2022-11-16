package economy.reverse.repurchase.agreement;

import college.myplugs.spring.MyMapperScans;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("economy.reverse.repurchase.agreement.dao")
@SpringBootApplication
@MyMapperScans
public class ReverseRepurchaseAgreementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReverseRepurchaseAgreementApplication.class, args);
	}

}
