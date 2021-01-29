package cz.muni.ics.kypo.rest;

import cz.muni.ics.kypo.service.AdaptivePhasesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adaptive-phases")
public class AdaptivePhasesRestController {

    private AdaptivePhasesService adaptivePhasesService;

    private AdaptivePhasesRestController(AdaptivePhasesService adaptivePhasesService) {
        this.adaptivePhasesService = adaptivePhasesService;
    }
}
