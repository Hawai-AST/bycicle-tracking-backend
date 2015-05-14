package de.hawai.bicycle_tracking.server.rest;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.astcore.tourmanagement.AddTourFailedException;
import de.hawai.bicycle_tracking.server.astcore.tourmanagement.ITour;
import de.hawai.bicycle_tracking.server.dto.TourDTO;
import de.hawai.bicycle_tracking.server.dto.TourListEntryDTO;
import de.hawai.bicycle_tracking.server.facade.Facade;
import de.hawai.bicycle_tracking.server.rest.exceptions.MalformedRequestException;
import de.hawai.bicycle_tracking.server.rest.exceptions.NotFoundException;
import de.hawai.bicycle_tracking.server.security.SessionService;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.GPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TourController {
    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    static {
        FORMAT.setLenient(false);
    }

    @Autowired
    private SessionService sessionService;

    @Autowired
    private Facade facade;

    @RequestMapping(value = "/v1/route", method = RequestMethod.POST,
            consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public TourDTO addRoute(@RequestBody TourDTO inTour) throws AddTourFailedException {

        TourDTO outTour = new TourDTO();
        if (inTour.name == null){
            throw new MalformedRequestException("Name missing");
        } else if (inTour.bikeID == null){
            throw new MalformedRequestException("BikeId missing");
        } else if (inTour.lengthInKm == null){
            throw new MalformedRequestException("LengthInKm missing");
        } else if (inTour.startAt == null){
            throw new MalformedRequestException("StartAt missing");
        } else if (inTour.finishedAt == null){
            throw new MalformedRequestException("FinishedAt missing");
        } else if (inTour.waypoints == null){
            throw new MalformedRequestException("Waypoints missing");
        }

        for (GPS gps: inTour.waypoints){
            if (!(-90.0 <= gps.getLatitude() && gps.getLatitude() <= 90)){
                throw new MalformedRequestException("Unknown Latitude");
            } else if (!(-180.0 <= gps.getLongitude() && gps.getLongitude() <= 180)){
                throw new MalformedRequestException("Unknown Logitude");
            } else if (gps.getName() == null){
                throw new MalformedRequestException("Unknown Position Name");
            }
        }
        IBike bike = facade.getBikeById(inTour.bikeID);
        if (bike == null){
            throw new NotFoundException("BikeId does not exists");
        }

        Date startAt = null;
        try {
            startAt = FORMAT.parse(inTour.startAt);
        } catch (ParseException e){
            throw new MalformedRequestException("StartAt ist not a Date");
        }

        Date finishedAt = null;
        try {
            finishedAt = FORMAT.parse(inTour.finishedAt);
        } catch (ParseException e){
            throw new MalformedRequestException("FinishedAt ist not a Date");
        }

        try {
            ITour tour = facade.addTour(
                    inTour.name,
                    bike,
                    startAt,
                    finishedAt,
                    inTour.waypoints,
                    inTour.lengthInKm
            );
            outTour.name = tour.getName();
            outTour.bikeID = tour.getBike().getId();
            outTour.createdAt = FORMAT.format(tour.getCreatedAt());
            outTour.finishedAt = FORMAT.format(tour.getFinishedAt());
            outTour.startAt = FORMAT.format(tour.getStartAt());
            outTour.updatedAt = FORMAT.format(tour.getUpdatedAt());
            outTour.lengthInKm = tour.getLengthInKm();
            outTour.id = tour.getId();
            outTour.waypoints = tour.getWaypoints();
        } catch (AddTourFailedException e) {
            throw new AddTourFailedException("Unable to Save Tour");
        }

        return outTour;
    }

    @RequestMapping(value = "/v1/route", method = RequestMethod.GET,
            consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public List<TourListEntryDTO> getRoute() {
        IUser user = facade.getUserBy(new EMail(sessionService.getCurrentlyLoggedinUser())).orElse(null);
        if (user == null){
            throw new NotFoundException("LoggedIn User not found");
        }
        List<ITour> tours = facade.getToursByUser(user);
        if (tours == null){
            throw new NotFoundException("User does not exists");
        }
        List<TourListEntryDTO> tourList = new ArrayList<>();
        for (ITour tour: tours){
            TourListEntryDTO dto = new TourListEntryDTO();
            dto.id = tour.getId();
            dto.name = tour.getName();
            dto.lengthInKm = tour.getLengthInKm();
            dto.bikeID = tour.getBike().getId();
            dto.startAt = FORMAT.format(tour.getStartAt());
            dto.finishedAt = FORMAT.format(tour.getFinishedAt());
            tourList.add(dto);
        }
        return tourList;

    }

    @RequestMapping(value = "/v1/route/{id}", method = RequestMethod.DELETE,
            consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    public void deleteRoute(@PathVariable long id) {
        ITour tour = facade.getTourById(id).orElse(null);
        if (tour == null){
            throw new NotFoundException("Bike does not exists");
        }
        facade.deleteTour(tour);

    }
}

