package cz.muni.ics.kypo.rest;

import cz.muni.ics.kypo.api.SuitableTaskResponseDto;
import cz.muni.ics.kypo.dto.AdaptiveSmartAssistantInput;
import cz.muni.ics.kypo.service.AdaptivePhasesService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/adaptive-phases")
public class AdaptivePhasesRestController {

    private AdaptivePhasesService adaptivePhasesService;

    @Autowired
    private AdaptivePhasesRestController(AdaptivePhasesService adaptivePhasesService) {
        this.adaptivePhasesService = adaptivePhasesService;
    }

    @PostMapping(path = "/")
    public ResponseEntity<SuitableTaskResponseDto> getSuitableTaskInPhase(@RequestBody @Valid AdaptiveSmartAssistantInput smartAssistantInput) {
        return ResponseEntity.ok(adaptivePhasesService.computeSuitableTask(smartAssistantInput));
    }
}
