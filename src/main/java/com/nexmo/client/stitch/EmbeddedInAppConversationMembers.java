package com.nexmo.client.stitch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ergyun Syuleyman on 2/15/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbeddedInAppConversationMembers {
    private InAppConversationMember[] conversationMembers;

    @JsonProperty("members")
    public void setInAppConversationMembers(InAppConversationMember[] conversationMembers) {
        this.conversationMembers = conversationMembers;
    }

    @JsonProperty("members")
    public InAppConversationMember[] getInAppConversationMembers() {
        return conversationMembers;
    }
}
