package com.nexmo.client.stitch;

import com.fasterxml.jackson.databind.util.ISO8601Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Ergyun Syuleyman on 2/14/18.
 */

public class InAppConversationsFilter {
    private String name;
    private String displayName;
    private Date dateCreate;
    private Integer pageSize;
    private Integer recordIndex;
    private String order;
    private String conversationUuid;
    private String apiKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getRecordIndex() {
        return recordIndex;
    }

    public void setRecordIndex(Integer recordIndex) {
        this.recordIndex = recordIndex;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getConversationUuid() {
        return conversationUuid;
    }

    public void setConversationUuid(String conversationUuid) {
        this.conversationUuid = conversationUuid;
    }
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public List<NameValuePair> toUrlParams() {
        List<NameValuePair> result = new ArrayList<NameValuePair>(10);
        conditionalAdd(result, "name", this.name);
        conditionalAdd(result, "display_name", this.displayName);
        conditionalAdd(result, "date_create", this.dateCreate);
        conditionalAdd(result, "page_size", this.pageSize);
        conditionalAdd(result, "record_index", this.recordIndex);
        conditionalAdd(result, "order", this.order);
        conditionalAdd(result, "uuid", this.conversationUuid);
        conditionalAdd(result, "api_key", this.apiKey);

        return result;
    }

    private void conditionalAdd(List<NameValuePair> params, String name, String value) {
        if (value != null) {
            params.add(new BasicNameValuePair(name, value));
        }
    }

    private void conditionalAdd(List<NameValuePair> params, String name, Date value) {
        if (value != null) {
            params.add(new BasicNameValuePair(name, ISO8601Utils.format(value)));
        }
    }

    private void conditionalAdd(List<NameValuePair> params, String name, Object value) {
        if (value != null) {
            params.add(new BasicNameValuePair(name, value.toString()));
        }
    }

}
