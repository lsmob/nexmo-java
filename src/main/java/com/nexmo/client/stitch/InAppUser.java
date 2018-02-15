package com.nexmo.client.stitch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

/**
 * Created by Ergyun Syuleyman on 2/14/18.
 */

/**
 * InAppUser encapsulates the information required to create an user using {@link InAppMessagingClient#createUser(InAppUser)}
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({ "_links"})
public class InAppUser {
    /* The user names*/
    private String name;

    public InAppUser() {}

    public InAppUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from InAppUser object.", jpe);
        }
    }

    public static InAppUser fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, InAppUser.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce InAppUser object from json.", jpe);
        }
    }
}
