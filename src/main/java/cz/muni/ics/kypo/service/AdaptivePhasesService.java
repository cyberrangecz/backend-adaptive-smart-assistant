package cz.muni.ics.kypo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdaptivePhasesService {

    @Autowired
    public AdaptivePhasesService() {

    }

    public double computeParticipantsPerformance() {
        // get all the essential data from kypo-elasticsearch-service and other services..
        return 0.0;
    }

    /**
     * @param participantsPerformance must be in interval <0,1>
     * @param numberOfTasksInPhaseX
     * @return
     */
    public int computeSuitableTask(double participantsPerformance, int numberOfTasksInPhaseX) {
        if (participantsPerformance == 0.0) {
            return numberOfTasksInPhaseX;
        } else {
            return ((int) (numberOfTasksInPhaseX * (1 - participantsPerformance))) + 1;
        }
    }

}
