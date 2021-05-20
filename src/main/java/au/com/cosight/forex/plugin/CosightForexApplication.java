package au.com.cosight.forex.plugin;


import au.com.cosight.sdk.annotation.EnableCosightRuntimeContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCosightRuntimeContext
public class CosightForexApplication {

	public static void main(String[] args) {
		SpringApplication.run(CosightForexApplication.class, args);
	}

}
