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
