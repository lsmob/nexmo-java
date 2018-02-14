package com.nexmo.client.stitch;

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
public class InAppConversationInfoPage implements Iterable<InAppConversationInfo> {
    private int count;
    private int pageSize;
    private int recordIndex;

    private PageLinks links;
    private EmbeddedInAppConversations embedded;

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
    public EmbeddedInAppConversations getEmbedded() {
        return embedded;
    }

    @Override
    public Iterator<InAppConversationInfo> iterator() {
        return new ArrayIterator<>(embedded.getInAppConversationInfos());
    }

    public static InAppConversationInfoPage fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.readValue(json, InAppConversationInfoPage.class);
        } catch (IOException jpe) {
            throw new NexmoUnexpectedException("Failed to produce json from InAppConversation object.", jpe);
        }
    }
}
