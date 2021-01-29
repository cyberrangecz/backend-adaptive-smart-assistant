package cz.muni.ics.kypo.service;

import cz.muni.ics.kypo.api.SuitableTaskResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdaptivePhasesService {

    @Autowired
    public AdaptivePhasesService() {

    }

    private double computeParticipantsPerformance(int phaseX) {
        // get all the essential data from kypo-elasticsearch-service and other services..
        return 0.0;
    }

    /**
     * @param phaseX      the phase X a participant is trying to get
     * @param phaseXTasks the number of tasks in the phase X
     * @return the suitable task in a phase x
     */
    public SuitableTaskResponseDto computeSuitableTask(int phaseX, int phaseXTasks) {
        SuitableTaskResponseDto suitableTaskResponseDto = new SuitableTaskResponseDto();
        double participantsPerformance = computeParticipantsPerformance(phaseX); // must be in interval <0,1>
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
