package de.hawai.bicycle_tracking.server.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserTestingController {
    @RequestMapping(value = "/v1/user/testing", method = RequestMethod.GET)
    public ResponseEntity<String> getAdminTest() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
