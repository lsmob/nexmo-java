package com.nexmo.client.stitch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ergyun Syuleyman on 2/14/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbeddedInAppConversations {
    private InAppConversationInfo[] conversationInfos;

    @JsonProperty("conversations")
    private void setInAppConversationInfos(InAppConversationInfo[] conversationInfos) {
        this.conversationInfos = conversationInfos;
    }

    @JsonProperty("conversations")
    public InAppConversationInfo[] getInAppConversationInfos() {
        return conversationInfos;
    }
}
