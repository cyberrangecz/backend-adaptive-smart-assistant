package cz.muni.ics.kypo.rest;

import cz.muni.ics.kypo.api.SuitableTaskResponseDto;
import cz.muni.ics.kypo.service.AdaptivePhasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adaptive-phases")
public class AdaptivePhasesRestController {

    private AdaptivePhasesService adaptivePhasesService;

    @Autowired
    private AdaptivePhasesRestController(AdaptivePhasesService adaptivePhasesService) {
        this.adaptivePhasesService = adaptivePhasesService;
    }

    @GetMapping(path = "/")
    public ResponseEntity<SuitableTaskResponseDto> getSuitableTaskInPhase(@RequestParam(value = "definitionId") long definitionId,
                                                                          @RequestParam(value = "instanceId") long instanceId,
                                                                          @RequestParam(value = "phaseX") int phaseX,
                                                                          @RequestParam(value = "phaseXTasks") int phaseXTasks) {
        return ResponseEntity.ok(adaptivePhasesService.computeSuitableTask(definitionId, instanceId, phaseX, phaseXTasks));
    }
}
