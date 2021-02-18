package cz.muni.ics.kypo.service;

import cz.muni.ics.kypo.api.SuitableTaskResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdaptivePhasesService {

    private final ElasticSearchApiService elasticSearchApiService;

    @Autowired
    public AdaptivePhasesService(ElasticSearchApiService elasticSearchApiService) {
        this.elasticSearchApiService = elasticSearchApiService;
    }

    private double computeParticipantsPerformance(long definitionId, long instanceId, int phaseX) {
        List<Map<String, Object>> eventsFromTrainingInstance = elasticSearchApiService.findAllEventsFromTrainingInstance(definitionId, instanceId);

        // TODO find the proper information

        // get all the essential data from kypo-elasticsearch-service and other services..
        return 0.0;
    }

    /**
     * @param phaseX      the phase X a participant is trying to get
     * @param phaseXTasks the number of tasks in the phase X
     * @return the suitable task in a phase x
     */
    public SuitableTaskResponseDto computeSuitableTask(long definitionId, long instanceId, int phaseX, int phaseXTasks) {
        SuitableTaskResponseDto suitableTaskResponseDto = new SuitableTaskResponseDto();
        double participantsPerformance = computeParticipantsPerformance(definitionId, instanceId, phaseX); // must be in interval <0,1>
        if (participantsPerformance == 0.0) {
            suitableTaskResponseDto.setSuitableTask(phaseXTasks);
            return suitableTaskResponseDto;
        } else {
            int suitableTask = ((int) (phaseXTasks * (1 - participantsPerformance))) + 1;
            suitableTaskResponseDto.setSuitableTask(suitableTask);
            return suitableTaskResponseDto;
        }
    }

}
