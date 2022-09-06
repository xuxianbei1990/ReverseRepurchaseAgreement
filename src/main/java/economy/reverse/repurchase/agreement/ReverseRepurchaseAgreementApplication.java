package economy.reverse.repurchase.agreement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("economy.reverse.repurchase.agreement.dao")
@SpringBootApplication
public class ReverseRepurchaseAgreementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReverseRepurchaseAgreementApplication.class, args);
	}

}
