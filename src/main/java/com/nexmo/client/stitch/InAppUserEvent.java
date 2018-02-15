package com.nexmo.client.stitch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

/**
 * Created by Ergyun Syuleyman on 2/14/18.
 */

public class InAppUserEvent {
    /* Unique identifier for the user */
    private String id;
    private String href;

    public static InAppUserEvent fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, InAppUserEvent.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce InAppUserEvent object from json.", jpe);
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
