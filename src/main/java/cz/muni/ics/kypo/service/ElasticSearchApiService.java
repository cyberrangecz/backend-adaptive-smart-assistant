package cz.muni.ics.kypo.service;

import cz.muni.ics.kypo.api.dto.OverallPhaseStatistics;
import cz.muni.ics.kypo.exceptions.CustomWebClientException;
import cz.muni.ics.kypo.exceptions.MicroserviceApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ElasticSearchApiService {

    private final WebClient elasticsearchServiceWebClient;

    @Autowired
    public ElasticSearchApiService(@Qualifier("elasticsearchServiceWebClient") WebClient elasticsearchServiceWebClient) {
        this.elasticsearchServiceWebClient = elasticsearchServiceWebClient;
    }

    public List<OverallPhaseStatistics> getOverAllPhaseStatistics(long trainingRunId, List<Long> phaseIds, String accessToken, Long userId) {
        try {
            return elasticsearchServiceWebClient
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/training-statistics/training-runs/{runId}/phases/overall")
                            .queryParam("phaseIds", StringUtils.collectionToDelimitedString(phaseIds, ","))
                            .queryParam("accessToken", accessToken)
                            .queryParam("userId", userId)
                            .build(trainingRunId)
                    )
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<OverallPhaseStatistics>>() {
                    })
                    .block();
        } catch (CustomWebClientException ex) {
            throw new MicroserviceApiException("Could not retrieve wrong answers statistics from elastic for training run " + trainingRunId + ".", ex);
        }
    }
}
