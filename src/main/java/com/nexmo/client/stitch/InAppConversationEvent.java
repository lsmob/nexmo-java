package com.nexmo.client.stitch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

/**
 * Created by Ergyun Syuleyman on 2/14/18.
 */

public class InAppConversationEvent {
    /* Unique identifier for the conversation */
    private String id;
    private String href;

    public static InAppConversationEvent fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, InAppConversationEvent.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from InAppConversation object.", jpe);
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
