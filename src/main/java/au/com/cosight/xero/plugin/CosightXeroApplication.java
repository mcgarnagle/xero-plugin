package au.com.cosight.xero.plugin;


import au.com.cosight.sdk.annotation.EnableCosightRuntimeContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCosightRuntimeContext
public class CosightXeroApplication {

	public static void main(String[] args) {
		SpringApplication.run(CosightXeroApplication.class, args);
	}

}
