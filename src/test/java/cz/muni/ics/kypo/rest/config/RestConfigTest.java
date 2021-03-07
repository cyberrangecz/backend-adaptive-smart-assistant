package cz.muni.ics.kypo.rest.config;

import cz.muni.ics.kypo.service.ElasticSearchApiService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ComponentScan(basePackages = "cz.muni.ics.kypo.service")
public class RestConfigTest {

    @Bean
    @Qualifier("elasticsearchServiceWebClient")
    public WebClient elasticsearchServiceWebClient(){
        return WebClient.builder()
                .exchangeFunction(elasticsearchExchangeFunction())
                .build();
    }

    @Bean
    @Qualifier("elasticsearchExchangeFunction")
    public ExchangeFunction elasticsearchExchangeFunction(){
        return Mockito.mock(ExchangeFunction.class);
    }

    @Bean
    @Primary
    public ElasticSearchApiService elasticsearchApiServiceMock(){
        return Mockito.mock(ElasticSearchApiService.class);
    }

}
