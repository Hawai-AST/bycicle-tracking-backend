package de.hawai.bicycle_tracking.server.rest;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.ISellingLocation;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.dto.BikeDTO;
import de.hawai.bicycle_tracking.server.facade.Facade;
import de.hawai.bicycle_tracking.server.security.SessionService;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BikeController {
    private final DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private Facade facade;

    @Autowired
    private SessionService sessionService;

    @RequestMapping(value = "/v1/saleslocations", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public SalesLocationsResponse getSalesLocations() {
        Collection<? extends ISellingLocation> locations = facade.getAllSellingLocations();
        SalesLocationsResponse response = new SalesLocationsResponse();
        response.setAmount(locations.size());
        response.setLocations(locations.toArray(new ISellingLocation[locations.size()]));
        return response;
    }

    @RequestMapping(value = "/v1/bike", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<BikeDTO> createBike(BikeDTO inBike) {
        try {
            Date purchaseDate = mDateFormat.parse(inBike.getPurchaseDate());
            Date maintenanceDate = mDateFormat.parse(inBike.getNextMaintenance());
            IBike created = facade.createBike(inBike.getType(), new FrameNumber(inBike.getFrameNumber()), purchaseDate, maintenanceDate, null, null);
            BikeDTO response = new BikeDTO();
            response.setId(facade.getIdOfBike(created));
            response.setFrameNumber(created.getFrameNumber().getNumber());
            response.setNextMaintenance(mDateFormat.format(created.getNextMaintenanceDate()));
            response.setPurchaseDate(mDateFormat.format(created.getBuyDate()));
            response.setType(created.getType());
            response.setSalesLocation(created.getSoldLocation() != null ? created.getSoldLocation().getName() : null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/v1/bikes", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public BikesResponse getBikes() {
        String email = this.sessionService.getCurrentlyLoggedinUser();
        List<? extends IBike> bikes = facade.findByOwner(facade.getUserBy(new EMail(email)).get());
        BikesResponse response = new BikesResponse();
        BikeDTO[] dtos = new BikeDTO[bikes.size()];
        IBike current;
        for (int i = 0; i < bikes.size(); i++) {
            BikeDTO dto = new BikeDTO();
            current = bikes.get(i);
            dto.setId(facade.getIdOfBike(current));
            dto.setFrameNumber(current.getFrameNumber().getNumber());
            dto.setNextMaintenance(mDateFormat.format(current.getNextMaintenanceDate()));
            dto.setPurchaseDate(mDateFormat.format(current.getBuyDate()));
            dto.setSalesLocation(current.getSoldLocation() != null ? current.getSoldLocation().getName() : null);
            dto.setType(current.getType());
            dtos[i] = dto;
        }

        response.setAmount(dtos.length);
        response.setBikes(dtos);
        return response;
    }

    @RequestMapping(value = "/v1/bike/{id}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<BikeDTO> updateBike(@PathVariable long id, @RequestBody BikeDTO inNew) {
        IBike old = facade.getBikeById(id);
        if (old == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String email = this.sessionService.getCurrentlyLoggedinUser();
        IUser owner = facade.getUserBy(new EMail(email)).get();
        if (!old.getOwner().equals(owner)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            Date purchaseDate = mDateFormat.parse(inNew.getPurchaseDate());
            Date maintenanceDate = mDateFormat.parse(inNew.getNextMaintenance());
            facade.updateBike(old, inNew.getType(), new FrameNumber(inNew.getFrameNumber()), purchaseDate, maintenanceDate, null, owner);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private static class SalesLocationsResponse {
        private int amount;
        private ISellingLocation[] locations;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public ISellingLocation[] getLocations() {
            return locations;
        }

        public void setLocations(ISellingLocation[] locations) {
            this.locations = locations;
        }
    }

    private static class BikesResponse {
        private int amount;
        private BikeDTO[] bikes;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public BikeDTO[] getBikes() {
            return bikes;
        }

        public void setBikes(BikeDTO[] bikes) {
            this.bikes = bikes;
        }
    }
}
