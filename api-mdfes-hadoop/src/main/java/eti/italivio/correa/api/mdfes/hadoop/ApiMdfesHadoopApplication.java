package eti.italivio.correa.api.mdfes.hadoop;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiMdfesHadoopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiMdfesHadoopApplication.class, args);
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API OCR/PEDÁGIO PLACAS LIBERADAS")
                        .description("API para acesso as leituras OCR de placas liberadas")
                        .version("v1.0.0"));
    }
}
