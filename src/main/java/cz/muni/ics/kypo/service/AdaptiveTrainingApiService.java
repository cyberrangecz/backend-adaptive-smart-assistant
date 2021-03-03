package cz.muni.ics.kypo.service;

import cz.muni.ics.kypo.exceptions.ElasticsearchServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
public class AdaptiveTrainingApiService {

    private final WebClient adaptiveTrainingWebClient;

    @Autowired
    public AdaptiveTrainingApiService(@Qualifier("adaptiveTrainingWebClient") WebClient adaptiveTrainingWebClient) {
        this.adaptiveTrainingWebClient = adaptiveTrainingWebClient;
    }

    // TODO won't be used. However, not sure yet whether there will be different REST API calls to training definition
    public void getDecisionMatrix(long trainingDefinitionId, long phaseId) {
        try {
            adaptiveTrainingWebClient
                    .get()
                    .uri("/training-definitions/{definitionId}/phases/{phaseId}", trainingDefinitionId, phaseId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<Long, Boolean>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            throw new ElasticsearchServiceException("Could not retrieve all events from elastic for training definition ID: " + trainingDefinitionId, e);
        }
    }
}
