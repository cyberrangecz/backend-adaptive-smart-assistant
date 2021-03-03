package cz.muni.ics.kypo.rest;

import cz.muni.ics.kypo.api.SuitableTaskResponseDto;
import cz.muni.ics.kypo.dto.AdaptiveSmartAssistantInput;
import cz.muni.ics.kypo.service.AdaptivePhasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/adaptive-phases")
public class AdaptivePhasesRestController {

    private final AdaptivePhasesService adaptivePhasesService;

    @Autowired
    private AdaptivePhasesRestController(AdaptivePhasesService adaptivePhasesService) {
        this.adaptivePhasesService = adaptivePhasesService;
    }

    @PostMapping
    public ResponseEntity<SuitableTaskResponseDto> getSuitableTaskInPhase(@RequestBody @Valid AdaptiveSmartAssistantInput smartAssistantInput) {
        return ResponseEntity.ok(adaptivePhasesService.computeSuitableTask(smartAssistantInput));
    }
}
