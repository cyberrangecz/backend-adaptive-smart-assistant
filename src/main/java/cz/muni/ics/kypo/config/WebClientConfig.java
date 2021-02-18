package cz.muni.ics.kypo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${elasticsearch-service.uri}")
    private String elasticsearchServiceURI;


    /**
     * Elasticsearch service web client.
     *
     * @return the web client
     */
    @Bean
    @Qualifier("elasticsearchServiceWebClient")
    public WebClient elasticsearchServiceWebClient() {
        return WebClient.builder()
                .baseUrl(elasticsearchServiceURI)
                .defaultHeaders(headers -> {
                    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .build();
    }

}
