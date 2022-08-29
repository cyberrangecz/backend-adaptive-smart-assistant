package cz.muni.ics.kypo.rest;

import cz.muni.ics.kypo.api.dto.AdaptiveSmartAssistantInput;
import cz.muni.ics.kypo.api.dto.OverallInstancePerformance;
import cz.muni.ics.kypo.api.dto.SuitableTaskResponseDto;
import cz.muni.ics.kypo.service.AdaptivePhasesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/adaptive-phases")
@Api(value = "/adaptive-phases",
        tags = "Adaptive Phases",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        authorizations = @Authorization(value = "bearerAuth"))
@RequiredArgsConstructor
public class AdaptivePhasesRestController {

    private final AdaptivePhasesService adaptivePhasesService;

    @ApiOperation(httpMethod = "POST",
            value = "Find a suitable task",
            notes = "Find a suitable task for a participant trying to get the next phase",
            response = SuitableTaskResponseDto.class,
            nickname = "findSuitableTaskInPhase"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suitable task found."),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PostMapping
    public ResponseEntity<SuitableTaskResponseDto> findSuitableTaskInPhase(
            @ApiParam(value = "smartAssistantInput", required = true) @RequestBody @Valid AdaptiveSmartAssistantInput smartAssistantInput,
            @ApiParam(value = "Training instance access token") @RequestParam(name = "accessToken", required = false) String accessToken,
            @ApiParam(value = "User ID") @RequestParam(name = "userId", required = false) Long userId) {
        return ResponseEntity.ok(adaptivePhasesService.computeSuitableTask(smartAssistantInput, accessToken, userId));
    }

    @ApiOperation(httpMethod = "POST",
            value = "Find a suitable task",
            notes = "Find a suitable tasks for participants based on their performance",
            response = SuitableTaskResponseDto.class,
            nickname = "findSuitableTaskInPhase",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Suitable task found."),
            @ApiResponse(code = 500, message = "Unexpected application error")
    })
    @PostMapping(path = "/instances")
    public ResponseEntity<List<List<SuitableTaskResponseDto>>> findSuitableTasksInPhase(
            @ApiParam(value = "smartAssistantInput", required = true) @RequestBody @Valid List<OverallInstancePerformance> overallInstancePerformances) {
        return ResponseEntity.ok(adaptivePhasesService.computeSuitableTask(overallInstancePerformances));
    }
}
