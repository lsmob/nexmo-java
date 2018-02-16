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

package com.nexmo.client.stitch.members;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoMethodFailedException;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.stitch.Constants;
import com.nexmo.client.stitch.EmbeddedInAppConversationMembers;
import com.nexmo.client.stitch.InAppConversationMember;
import com.nexmo.client.stitch.InAppConversationMembersPage;
import com.nexmo.client.voice.endpoints.AbstractMethod;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

/**
 * Created by Ergyun Syuleyman on 2/15/18.
 */

public class ListConversationMembersMethod extends AbstractMethod<String, InAppConversationMembersPage> {
    private static final Log LOG = LogFactory.getLog(ListConversationMembersMethod.class);

    private static final String DEFAULT_URI = "https://api.nexmo.com/beta/conversations/:id/members";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};
    private String uri = DEFAULT_URI;
    private String conversationId;

    public ListConversationMembersMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(String conversationId) throws NexmoClientException, UnsupportedEncodingException {
        this.conversationId = conversationId;
        URIBuilder uriBuilder;
        String finalUri = uri;
        if (conversationId != null) {
            finalUri = finalUri.replace(":id", conversationId);
        } else {
            throw new NexmoMethodFailedException("Not allowed request. Conversation ID is required!");
        }
        try {
            uriBuilder = new URIBuilder(finalUri);
        } catch (URISyntaxException e) {
            throw new NexmoUnexpectedException("Could not parse URI: " + this.uri);
        }

        LOG.debug("Application Conversation Members URL: " + uriBuilder.toString() );

        return RequestBuilder.get().setUri(uriBuilder.toString());
    }

    @Override
    public InAppConversationMembersPage parseResponse(HttpResponse response) throws IOException {
        String json;
        InAppConversationMembersPage conversationMembersPage;
        final StatusLine statusLine = response.getStatusLine();
        try {
            json = new BasicResponseHandler().handleResponse(response);
        } catch (HttpResponseException e) {
            json = "{}";
            LOG.error("Application Conversation Members response: " + response.toString(), e);
        }

        LOG.debug("Application Conversation Members JSON: " + json );

        if (Constants.enableConversationMemberssListPagination) {
            conversationMembersPage = InAppConversationMembersPage.fromJson(json);
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            EmbeddedInAppConversationMembers embeddedInAppConversationMembers = new EmbeddedInAppConversationMembers();
            embeddedInAppConversationMembers.setInAppConversationMembers(mapper.readValue(json, InAppConversationMember[].class));
            conversationMembersPage = new InAppConversationMembersPage();
            conversationMembersPage.setEmbedded(embeddedInAppConversationMembers);
        }
        conversationMembersPage.setConversationId(this.conversationId);
        conversationMembersPage.setStatusCode(statusLine.getStatusCode());
        conversationMembersPage.setReasonPhrase(statusLine.getReasonPhrase());
        return conversationMembersPage;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return this.uri;
    }
}
