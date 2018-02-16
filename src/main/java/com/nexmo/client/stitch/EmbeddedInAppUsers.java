package com.nexmo.client.stitch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Ergyun Syuleyman on 2/14/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbeddedInAppUsers {
    private InAppUserInfo[] userInfos;

    @JsonProperty("users")
    public void setInAppUserInfos(InAppUserInfo[] userInfos) {
        this.userInfos = userInfos;
    }

    @JsonProperty("users")
    public InAppUserInfo[] getInAppUserInfos() {
        return userInfos;
    }
}
