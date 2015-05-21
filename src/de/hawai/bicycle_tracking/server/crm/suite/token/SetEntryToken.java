package de.hawai.bicycle_tracking.server.crm.suite.token;

import com.fasterxml.jackson.annotation.JsonRawValue;


public class SetEntryToken implements Token {
    private final String session;
    private final String module;
    
    @JsonRawValue
    private final String name_value_list;

    public SetEntryToken(String session, String module, String name_value_list) {
        this.session = session;
        this.module = module;
        this.name_value_list = name_value_list;
    }
}
