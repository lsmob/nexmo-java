package com.nexmo.client.stitch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

/**
 * Created by Ergyun Syuleyman on 2/14/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class InAppUserInfo {
    /* Unique identifier for the user */
    private String id;
    /* The name of the user */
    private String name;
    /* The href of the user */
    private String href;
    /* The user channels */
    private ChannelData channels;

    private int statusCode;
    private String reasonPhrase;

    public String toString() {
        return new StringBuilder()
                .append("<InAppUserInfo ")
                .append("ID: ").append(this.getId()).append(", ")
                .append("Name: ").append(this.getName()).append(", ")
                .append("HRef: ").append(this.getHRef()).append(", ")
                .append("Channels: ").append(this.getChannels())
                .append(">")
                .toString();
    }

    public static InAppUserInfo fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, InAppUserInfo.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce InAppUserInfo object from json.", jpe);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("href")
    public String getHRef() {
        return href;
    }

    public void setHRef(String href) {
        this.href = href;
    }

    @JsonProperty("channels")
    public ChannelData getChannels() {
        return channels;
    }

    public void setChannels(ChannelData channels) {
        this.channels = channels;
    }


    @JsonIgnore
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @JsonIgnore
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }
}
