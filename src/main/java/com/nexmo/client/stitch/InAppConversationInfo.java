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
public class InAppConversationInfo {
    /* Unique identifier for the conversation */
    private String uuid;
    /* The name of the conversation */
    private String name;
    /* The display name of the conversation */
    private String displayName;
    private Integer sequenceNumber;
    private String numbers;
    private String properties;
    /* The API Key of the owner of the conversation */
    private String apiKey;

    private int statusCode;
    private String reasonPhrase;

    public String toString() {
        return new StringBuilder()
                .append("<InAppConversationInfo ")
                .append("ID: ").append(this.getUuid()).append(", ")
                .append("Name: ").append(this.getName()).append(", ")
                .append("Display-Name: ").append(this.getDisplayName()).append(", ")
                .append("Sequence_Number: ").append(this.getSequenceNumber()).append(", ")
                .append("Numbers: ").append(this.getNumbers()).append(", ")
                .append("Properties: ").append(this.getProperties()).append(", ")
                .append("Api-Key: ").append(this.getApiKey())
                .append(">")
                .toString();
    }

    public static InAppConversationInfo fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, InAppConversationInfo.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce InAppConversationInfo object from json.", jpe);
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("display_name")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("sequence_number")
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    @JsonProperty("api_key")
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
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
