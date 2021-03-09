package cz.muni.ics.kypo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.ics.kypo.api.exceptions.error.JavaApiError;
import cz.muni.ics.kypo.exceptions.CustomWebClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Configuration
public class WebClientConfig {

    @Value("${elasticsearch-service.uri}")
    private String elasticsearchServiceURI;

    private final ObjectMapper objectMapper;

    @Autowired
    public WebClientConfig(@Qualifier("webClientObjectMapper") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

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
                .filters(exchangeFilterFunctions -> {
//                    exchangeFilterFunctions.add(addSecurityHeader());
                    exchangeFilterFunctions.add(javaMicroserviceExceptionHandlingFunction());
                })
                .build();
    }

//    private ExchangeFilterFunction addSecurityHeader() {
//        return (request, next) -> {
//            OAuth2Authentication authenticatedUser = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
//            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authenticatedUser.getDetails();
//            ClientRequest filtered = ClientRequest.from(request)
//                    .header("Authorization", "Bearer " + details.getTokenValue())
//                    .build();
//            return next.exchange(filtered);
//        };
//    }

    private ExchangeFilterFunction javaMicroserviceExceptionHandlingFunction() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if(clientResponse.statusCode().is4xxClientError() || clientResponse.statusCode().is5xxServerError()) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            if (errorBody == null || errorBody.isBlank()) {
                                throw new CustomWebClientException("Error from external microservice. No specific message provided.", clientResponse.statusCode());
                            }
                            JavaApiError javaApiError = null;
                            try {
                                javaApiError = objectMapper.readValue(errorBody, JavaApiError.class);
                                javaApiError.setStatus(clientResponse.statusCode());
                            } catch (IOException e) {
                                throw new CustomWebClientException("Error from external microservice. No specific message provided.", clientResponse.statusCode());
                            }
                            throw new CustomWebClientException(javaApiError);
                        });
            } else {
                return Mono.just(clientResponse);
            }
        });
    }
}
