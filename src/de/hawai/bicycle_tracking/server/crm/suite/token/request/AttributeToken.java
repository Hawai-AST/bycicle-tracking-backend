package de.hawai.bicycle_tracking.server.crm.suite.token.request;

import de.hawai.bicycle_tracking.server.crm.suite.token.Token;

public class AttributeToken implements Token {
	
    private final String name;
    private final String value;

    public AttributeToken(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
