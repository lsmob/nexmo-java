package com.nexmo.client.stitch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

/**
 * Created by Ergyun Syuleyman on 2/15/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class InAppConversationMember {
    /* Unique identifier for the conversation */
    private String conversationId;
    /* The name|id of the member */
    private String name;
    /* The display name of the user */
    private String userName;
    /* The user ID */
    private String userId;
    /* The member state */
    private InAppConversationMemberState state;
    /* The member channel */
    private ChannelData channel;
    /* The href of the conversation member */
    private String href;

    public String toString() {
        return new StringBuilder()
                .append("<InAppConversationMember ")
                .append("Conversation-ID: ").append(this.getConversationId()).append(", ")
                .append("Name: ").append(this.getName()).append(", ")
                .append("User-Name: ").append(this.getUserName()).append(", ")
                .append("User-ID: ").append(this.getUserId()).append(", ")
                .append("State: ").append(this.getState()).append(", ")
                .append("Channel: ").append(this.getChannel()).append(", ")
                .append("href: ").append(this.getHRef())
                .append(">")
                .toString();
    }

    public static InAppConversationMember fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, InAppConversationMember.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce InAppConversationMember object from json.", jpe);
        }
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("state")
    public InAppConversationMemberState getState() {
        return state;
    }

    public void setChannel(InAppConversationMemberState state) {
        this.state = state;
    }

    @JsonProperty("channel")
    public ChannelData getChannel() {
        return channel;
    }

    public void setChannel(ChannelData channel) {
        this.channel = channel;
    }

    @JsonProperty("href")
    public String getHRef() {
        return href;
    }

    public void setHRef(String href) {
        this.href = href;
    }
}
