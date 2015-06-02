package de.hawai.bicycle_tracking.server.rest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.BikeType;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBikeType;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBikeTypeDao;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.ISellingLocation;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.dto.BikeDTO;
import de.hawai.bicycle_tracking.server.dto.BikeTypeDTO;
import de.hawai.bicycle_tracking.server.facade.Facade;
import de.hawai.bicycle_tracking.server.rest.exceptions.AlreadyExistsException;
import de.hawai.bicycle_tracking.server.rest.exceptions.InvalidAccessException;
import de.hawai.bicycle_tracking.server.rest.exceptions.MalformedRequestException;
import de.hawai.bicycle_tracking.server.security.SessionService;
import de.hawai.bicycle_tracking.server.utility.DateFormatUtil;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

@RestController
@RequestMapping("/api")
public class BikeController {
	private final DateFormat mDateFormat = DateFormatUtil.DEFAULT_FORMAT;

	@Autowired
	private Facade facade;

	@Autowired
	private IBikeTypeDao bikeTypeRepository;

	@Autowired
	private SessionService sessionService;

	@RequestMapping(value = "/v1/biketypes", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public BikeTypesResponse getBikeTypes() {
		List<? extends IBikeType> bikeTypes =  bikeTypeRepository.findAll();
		BikeTypesResponse response = new BikeTypesResponse();
		response.setAmount(bikeTypes.size());
		List<BikeTypeDTO> bikeTypeDTOs = new ArrayList<>();
		for (IBikeType bikeType : bikeTypes) {
			bikeTypeDTOs.add(new BikeTypeDTO(bikeType));
		}
		response.setBikeTypes(bikeTypeDTOs);
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		try {
			System.err.println(mapper.writeValueAsString(bikeTypeDTOs));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping(value = "/v1/saleslocations", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public SalesLocationsResponse getSalesLocations() {
		Collection<? extends ISellingLocation> locations = facade.getAllSellingLocations();
		SalesLocationsResponse response = new SalesLocationsResponse();
		response.setAmount(locations.size());
		response.setLocations(locations.toArray(new ISellingLocation[locations.size()]));
		return response;
	}

	@RequestMapping(value = "/v1/bike", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<BikeDTO> createBike(@RequestBody BikeDTO inBike) {
		//    	TODO(fap): this is stupid, we only need this because the type that we get in
		//    	is detatched in the hibernate/jpa world.
		//    	Maybe only call this line if crm is off?
		BikeType type = bikeTypeRepository.findOne(inBike.getType());
		Date purchaseDate;
		Date maintenanceDate;
		try {
			purchaseDate = mDateFormat.parse(inBike.getPurchaseDate());
			maintenanceDate = mDateFormat.parse(inBike.getNextMaintenance());
		} catch (Exception e) {
			throw new MalformedRequestException("Invalid date detected");
		}

		String email = this.sessionService.getCurrentlyLoggedinUser();
		IBike created;
		try {
			created = facade.createBike(type, new FrameNumber(inBike.getFrameNumber()), purchaseDate, maintenanceDate, null,
					facade.getUserBy(new EMail(email)).get(), inBike.getName());
		} catch (ConstraintViolationException e) {
			throw new AlreadyExistsException("Bike with the framenumber exists already.");
		}

		BikeDTO response = new BikeDTO();
		response.setId(facade.getIdOfBike(created));
		response.setFrameNumber(created.getFrameNumber().getNumber());
		response.setNextMaintenance(mDateFormat.format(created.getNextMaintenanceDate()));
		response.setPurchaseDate(mDateFormat.format(created.getPurchaseDate()));
		response.setType(created.getType().getId());
		response.setSalesLocation(created.getSoldLocation() != null ? created.getSoldLocation().getName() : null);
		return new ResponseEntity<>(response, HttpStatus.OK);
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
			dto.setPurchaseDate(mDateFormat.format(current.getPurchaseDate()));
			dto.setSalesLocation(current.getSoldLocation() != null ? current.getSoldLocation().getName() : null);
			dto.setType(current.getType().getId());
			dtos[i] = dto;
		}

		response.setAmount(dtos.length);
		response.setBikes(dtos);
		return response;
	}

	@RequestMapping(value = "/v1/bike/{id}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<BikeDTO> updateBike(@PathVariable UUID id, @RequestBody BikeDTO inNew) {
		Optional<IBike> old = facade.getBikeById(id);
		if (!old.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		String email = this.sessionService.getCurrentlyLoggedinUser();
		IUser owner = facade.getUserBy(new EMail(email)).get();
		if (!old.get().getOwner().equals(owner)) {
			throw new InvalidAccessException("This bike does not belong to you.");
		}

		Date purchaseDate;
		Date maintenanceDate;

		try {
			purchaseDate = mDateFormat.parse(inNew.getPurchaseDate());
			maintenanceDate = mDateFormat.parse(inNew.getNextMaintenance());
		} catch (Exception e) {
			throw new MalformedRequestException("Invalid date detected");
		}

		facade.updateBike(old.get(), bikeTypeRepository.findOne(inNew.getType()),
				new FrameNumber(inNew.getFrameNumber()), purchaseDate, maintenanceDate, null, owner, inNew.getName());
		inNew.setId(id);
		return new ResponseEntity<>(inNew, HttpStatus.OK);
	}

	private static class BikeTypesResponse {
		private int amount;
		private List<BikeTypeDTO> bikeTypes;

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}

		public List<BikeTypeDTO> getBikeTypes() {
			return bikeTypes;
		}

		public void setBikeTypes(List<BikeTypeDTO> bikeTypes) {
			this.bikeTypes = bikeTypes;
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
