package de.hawai.bicycle_tracking.server.rest;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.dto.PasswordDTO;
import de.hawai.bicycle_tracking.server.dto.UserDTO;
import de.hawai.bicycle_tracking.server.facade.Facade;
import de.hawai.bicycle_tracking.server.rest.exceptions.MalformedRequestException;
import de.hawai.bicycle_tracking.server.security.SessionService;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class CustomerController {
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private SessionService sessionService;

    @Autowired
    private Facade facade;

    @RequestMapping(value = "/v1/user/password", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@RequestBody PasswordDTO inNewPassword) {
        String currentEmail = sessionService.getCurrentlyLoggedinUser();
        facade.updatePassword(facade.getUserBy(new EMail(currentEmail)).get(), inNewPassword.getCode());
    }

    @RequestMapping(value = "/v1/user", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserData(@RequestBody UserDTO inUser) {
        Date birthday;

        try {
            if (inUser.getBirthday() != null && inUser.getBirthday().length() > 0) {
                birthday = dateFormat.parse(inUser.getBirthday());
            } else {
                birthday = null;
            }
        } catch (Exception e) {
            throw new MalformedRequestException("Invalid birthdate");
        }

        String currentEmail = sessionService.getCurrentlyLoggedinUser();
        IUser currentUser = facade.getUserBy(new EMail(currentEmail)).get();
        facade.updateUser(currentUser, inUser.getName(), inUser.getFirstname(), birthday, inUser.getAddress());
    }

    @RequestMapping(value = "/v1/user", method = RequestMethod.GET, produces = "application/json")
    public IUser getUserDetails() {
        String currentEmail = sessionService.getCurrentlyLoggedinUser();
        return facade.getUserBy(new EMail(currentEmail)).get();
    }
}
