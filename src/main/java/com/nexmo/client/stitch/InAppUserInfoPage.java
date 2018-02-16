package com.nexmo.client.stitch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.legacyutils.PageLinks;

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
