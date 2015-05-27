package de.hawai.bicycle_tracking.server.crm.suite.token.request;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;

import de.hawai.bicycle_tracking.server.crm.suite.token.Token;


public class LoginToken implements Token {
    private Map<String, String> user_auth = new HashMap<>();

    public LoginToken(String suiteUser, String plainPWD) {
        user_auth.put("user_name", suiteUser);
        try {
            user_auth.put("password", Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(plainPWD.getBytes())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
