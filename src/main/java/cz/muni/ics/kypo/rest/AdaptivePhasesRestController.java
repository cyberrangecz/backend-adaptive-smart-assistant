package cz.muni.ics.kypo.rest;

import cz.muni.ics.kypo.api.dto.SuitableTaskResponseDto;
import cz.muni.ics.kypo.api.dto.AdaptiveSmartAssistantInput;
import cz.muni.ics.kypo.service.AdaptivePhasesService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/adaptive-phases")
@Api(value = "/adaptive-phases",
        tags = "Adaptive Phases",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        authorizations = @Authorization(value = "bearerAuth"))
public class AdaptivePhasesRestController {

    private final AdaptivePhasesService adaptivePhasesService;

    @Autowired
    public AdaptivePhasesRestController(AdaptivePhasesService adaptivePhasesService) {
        this.adaptivePhasesService = adaptivePhasesService;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Find a suitable task",
            notes = "Find a suitable task for a participant trying to get the next phase",
            response = SuitableTaskResponseDto.class,
            nickname = "findSuitableTaskInPhase",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suitable task found."),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PostMapping
    public ResponseEntity<SuitableTaskResponseDto> findSuitableTaskInPhase(
            @ApiParam(value = "smartAssistantInput", required = true)
            @RequestBody @Valid AdaptiveSmartAssistantInput smartAssistantInput) {
        return ResponseEntity.ok(adaptivePhasesService.computeSuitableTask(smartAssistantInput));
    }
}
