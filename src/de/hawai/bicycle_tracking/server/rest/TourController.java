package de.hawai.bicycle_tracking.server.rest;

import de.hawai.bicycle_tracking.server.dto.TourDTO;
import de.hawai.bicycle_tracking.server.facade.Facade;
import de.hawai.bicycle_tracking.server.security.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api")
public class TourController {
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private SessionService sessionService;

    @Autowired
    private Facade facade;

    @RequestMapping(value = "/v1/route", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public TourDTO addRoute(@RequestBody TourDTO inTour){
        System.out.println(inTour);
        return inTour;
    }
}

