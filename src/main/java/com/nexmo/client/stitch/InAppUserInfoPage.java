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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.legacyutils.PageLinks;
import com.nexmo.client.stitch.commons.Constants;
import com.nexmo.client.stitch.users.EmbeddedInAppUsers;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Ergyun Syuleyman on 2/13/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class InAppUserInfoPage implements Iterable<InAppUserInfo> {
    private int count;
    private int pageSize;
    private int recordIndex;

    private PageLinks links;
    private EmbeddedInAppUsers embedded;

    private int statusCode;
    private String reasonPhrase;

    public int getCount() {
        return count;
    }

    @JsonProperty("page_size")
    public int getPageSize() {
        return pageSize;
    }

    @JsonProperty("record_index")
    public int getRecordIndex() {
        return recordIndex;
    }

    @JsonProperty("_links")
    public PageLinks getLinks() {
        return links;
    }

    @JsonProperty("_embedded")
    public EmbeddedInAppUsers getEmbedded() {
        return embedded;
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


    @Override
    public Iterator<InAppUserInfo> iterator() {
        return new ArrayIterator<>(embedded.getInAppUserInfos());
    }

    @JsonIgnore
    public void setEmbedded(EmbeddedInAppUsers embeddedInAppUsers) {
        if (!Constants.enableUsersListPagination) {
            this.embedded = embeddedInAppUsers;
            this.count = embeddedInAppUsers.getInAppUserInfos().length;
            this.pageSize = embeddedInAppUsers.getInAppUserInfos().length;
            this.recordIndex = 0;
        }
    }

    public static InAppUserInfoPage fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.readValue(json, InAppUserInfoPage.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce InAppUserInfoPage object from json.", jpe);
        }
    }
}
