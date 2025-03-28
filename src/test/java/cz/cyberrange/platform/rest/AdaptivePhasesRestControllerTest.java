package cz.cyberrange.platform.rest;

import cz.cyberrange.platform.api.dto.AdaptiveSmartAssistantInput;
import cz.cyberrange.platform.api.dto.SuitableTaskResponseDto;
import cz.cyberrange.platform.api.exceptions.error.ApiEntityError;
import cz.cyberrange.platform.handler.CustomRestExceptionHandler;
import cz.cyberrange.platform.rest.util.ObjectConverter;
import cz.cyberrange.platform.rest.util.TestDataFactory;
import cz.cyberrange.platform.service.AdaptivePhasesService;
import cz.cyberrange.platform.service.ElasticSearchApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ContextConfiguration(classes = {
        AdaptivePhasesRestController.class,
        TestDataFactory.class,
        AdaptivePhasesService.class
})
@WebMvcTest(AdaptivePhasesRestController.class)
class AdaptivePhasesRestControllerTest {

    private MockMvc mvc;
    @Autowired
    private AdaptivePhasesRestController adaptivePhasesRestController;
    @Autowired
    private TestDataFactory testDataFactory;
    @MockBean
    private ElasticSearchApiService elasticSearchApiService;

    @BeforeEach
    public void init() {
        this.mvc = MockMvcBuilders.standaloneSetup(adaptivePhasesRestController)
                .setControllerAdvice(new CustomRestExceptionHandler())
                .build();
    }

    @Test
    void findSuitableTaskFirstTrainingPhaseInTrainingDefinition() throws Exception {
        given(elasticSearchApiService.getOverAllPhaseStatistics(anyLong(), anyList(), anyString(), anyLong())).willReturn(Collections.emptyList());
        MockHttpServletResponse response = mvc.perform(post("/adaptive-phases")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ObjectConverter.convertObjectToJson(testDataFactory.getAdaptiveSmartAssistantInput1())))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        SuitableTaskResponseDto suitableTaskResponseDto = ObjectConverter.convertJsonToObject(response.getContentAsString(), SuitableTaskResponseDto.class);
        assertThat(suitableTaskResponseDto, is(equalTo(testDataFactory.getSuitableTaskResponseThree())));
    }

    @Test
    void findSuitableTaskSecondTrainingPhaseInTrainingDefinition() throws Exception {
        given(elasticSearchApiService.getOverAllPhaseStatistics(anyLong(), anyList(), anyString(), anyLong())).willReturn(testDataFactory.getOverallPhaseStatisticsAllCorrect());
        MockHttpServletResponse response = mvc.perform(post("/adaptive-phases")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ObjectConverter.convertObjectToJson(testDataFactory.getAdaptiveSmartAssistantInput2())))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        SuitableTaskResponseDto suitableTaskResponseDto = ObjectConverter.convertJsonToObject(response.getContentAsString(), SuitableTaskResponseDto.class);
        assertThat(suitableTaskResponseDto, is(equalTo(testDataFactory.getSuitableTaskResponseTwo())));
    }

    @Test
    void findSuitableTaskWorstPossiblePerformance() throws Exception {
        given(elasticSearchApiService.getOverAllPhaseStatistics(anyLong(), anyList(), anyString(), anyLong())).willReturn(testDataFactory.getOverallPhaseStatisticsAllWrong());
        MockHttpServletResponse response = mvc.perform(post("/adaptive-phases")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ObjectConverter.convertObjectToJson(testDataFactory.getAdaptiveSmartAssistantInput3())))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        SuitableTaskResponseDto suitableTaskResponseDto = ObjectConverter.convertJsonToObject(response.getContentAsString(), SuitableTaskResponseDto.class);
        assertThat(suitableTaskResponseDto, is(equalTo(testDataFactory.getSuitableTaskResponseThree())));
    }

    @Test
    void findSuitableTaskDifferentWeightsInMatrix() throws Exception {
        given(elasticSearchApiService.getOverAllPhaseStatistics(anyLong(), anyList(), anyString(), anyLong())).willReturn(testDataFactory.getOverallPhaseStatisticsCombinedPerformance());
        MockHttpServletResponse response = mvc.perform(post("/adaptive-phases")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ObjectConverter.convertObjectToJson(testDataFactory.getAdaptiveSmartAssistantInput4())))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        SuitableTaskResponseDto suitableTaskResponseDto = ObjectConverter.convertJsonToObject(response.getContentAsString(), SuitableTaskResponseDto.class);
        assertThat(suitableTaskResponseDto, is(equalTo(testDataFactory.getSuitableTaskResponseFive())));
    }

    @Test
    void findSuitableTaskZeroWeightsInDecisionMatrix() throws Exception {
        given(elasticSearchApiService.getOverAllPhaseStatistics(anyLong(), anyList(), anyString(), anyLong())).willReturn(Collections.emptyList());
        MockHttpServletResponse response = mvc.perform(post("/adaptive-phases")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ObjectConverter.convertObjectToJson(testDataFactory.getAdaptiveSmartAssistantInputZeroWeightsInDecisionMatrix())))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        SuitableTaskResponseDto suitableTaskResponseDto = ObjectConverter.convertJsonToObject(response.getContentAsString(), SuitableTaskResponseDto.class);
        assertThat(suitableTaskResponseDto, is(equalTo(testDataFactory.getSuitableTaskResponseThree())));
    }

    @Test
    void findSuitableTaskWrongPhaseIds() throws Exception {
        AdaptiveSmartAssistantInput adaptiveSmartAssistantInput = testDataFactory.getAdaptiveSmartAssistantInput2();
        adaptiveSmartAssistantInput.getDecisionMatrix().get(0).setCompletedInTime(1L);
        given(elasticSearchApiService.getOverAllPhaseStatistics(anyLong(), anyList(), anyString(), anyLong())).willReturn(testDataFactory.getOverallPhaseStatisticsWrongPhaseId());
        MockHttpServletResponse response = mvc.perform(post("/adaptive-phases")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(ObjectConverter.convertObjectToJson(adaptiveSmartAssistantInput)))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();
        ApiEntityError apiEntityError = ObjectConverter.convertJsonToObject(response.getContentAsString(), ApiEntityError.class);
        assertThat(HttpStatus.NOT_FOUND, is(apiEntityError.getStatus()));
        assertThat(apiEntityError.getMessage(), is("Statistics for phase not found"));
    }
}
