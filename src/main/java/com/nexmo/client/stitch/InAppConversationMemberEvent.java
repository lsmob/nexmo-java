/*
 * Copyright (c) 2018 SoftAvail Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.nexmo.client.stitch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoUnexpectedException;

import java.io.IOException;

/**
 * Created by Ergyun Syuleyman on 2/15/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class InAppConversationMemberEvent {
    /* Unique identifier for the conversation */
    private String conversationId;
    /* The id of the member */
    private String id;
    /* The user ID */
    private String userId;
    /* The member state */
    private InAppConversationMemberState state;
    /* The member channel */
    private ChannelData channel;
    /* The href of the conversation member */
    private String href;

    private int statusCode;
    private String reasonPhrase;

    public String toString() {
        return new StringBuilder()
                .append("<InAppConversationMemberEvent ")
                .append("Conversation-ID: ").append(this.getConversationId()).append(", ")
                .append("Member-ID: ").append(this.getId()).append(", ")
                .append("User-ID: ").append(this.getUserId()).append(", ")
                .append("State: ").append(this.getState()).append(", ")
                .append("Channel: ").append(this.getChannel()).append(", ")
                .append("href: ").append(this.getHref())
                .append(">")
                .toString();
    }

    public static InAppConversationMemberEvent fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, InAppConversationMemberEvent.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce InAppConversationMemberEvent object from json.", jpe);
        }
    }

    @JsonIgnore
    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public InAppConversationMemberState getState() {
        return state;
    }

    public void setState(InAppConversationMemberState state) {
        this.state = state;
    }

    public ChannelData getChannel() {
        return channel;
    }

    public void setChannel(ChannelData channel) {
        this.channel = channel;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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
