package com.nexmo.client.stitch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

/**
 * Created by Ergyun Syuleyman on 2/15/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class InAppConversationMemberRequest {
    /* The conversation ID */
    private String conversationId;
    /* The user ID */
    private String userId;
    /* The member action */
    private InAppConversationMemberAction action;
    /* The member channel */
    private ChannelData channel;

    public InAppConversationMemberRequest(String userId,
                                          InAppConversationMemberAction action,
                                          ChannelData channel) {
        this.userId = userId;
        this.action = action;
        this.channel = channel;
    }


    @JsonIgnore
    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("action")
    public InAppConversationMemberAction getAction() {
        return action;
    }

    public void setAction(InAppConversationMemberAction action) {
        this.action = action;
    }

    @JsonProperty("channel")
    public ChannelData getChannel() {
        return channel;
    }

    public void setChannel(ChannelData channel) {
        this.channel = channel;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from InAppConversationMemberRequest object.", jpe);
        }
    }
    public static InAppConversationMemberRequest fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, InAppConversationMemberRequest.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce InAppConversationMemberRequest object from json.", jpe);
        }
    }

}
