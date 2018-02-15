package com.nexmo.client.stitch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by Ergyun Syuleyman on 2/15/18.
 */

public enum InAppConversationMemberState {
    JOINED,
    LEAVED;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static InAppConversationMemberState fromString(String name) {
        return InAppConversationMemberState.valueOf(name.toUpperCase());
    }
}
