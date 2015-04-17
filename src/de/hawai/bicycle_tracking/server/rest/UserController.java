package de.hawai.bicycle_tracking.server.rest;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.facade.Facade;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private Facade facade;

    @RequestMapping(value = "/v1/user/{email:.+}", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    public IUser getUserByEmail(@PathVariable("email") String inEmail) {
        return facade.getUserBy(new EMail(inEmail)).get();
    }
}
