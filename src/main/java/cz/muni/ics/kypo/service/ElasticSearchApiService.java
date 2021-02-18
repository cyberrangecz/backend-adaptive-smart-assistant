package cz.muni.ics.kypo.service;

import cz.muni.ics.kypo.exceptions.ElasticsearchServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class ElasticSearchApiService {

    private final WebClient elasticsearchServiceWebClient;

    @Autowired
    public ElasticSearchApiService(@Qualifier("elasticsearchServiceWebClient") WebClient elasticsearchServiceWebClient) {
        this.elasticsearchServiceWebClient = elasticsearchServiceWebClient;
    }

    public List<Map<String, Object>> findAllEventsFromTrainingInstance(long definitionId, long instanceId) {
        try {
            return elasticsearchServiceWebClient
                    .get()
                    .uri("/adaptive-training-platform-events/training-definitions/{definitionId}/training-instances/{instanceId}", definitionId, instanceId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();
        } catch (WebClientResponseException e){
            throw new ElasticsearchServiceException("Could not retrieve all events from elastic for training instance ID: " + instanceId, e);
        }
    }
}
