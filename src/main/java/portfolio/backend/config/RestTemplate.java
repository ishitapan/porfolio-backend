package portfolio.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}