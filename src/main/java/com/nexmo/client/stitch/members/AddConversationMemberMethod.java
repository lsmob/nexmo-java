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

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoMethodFailedException;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.stitch.InAppConversationMemberEvent;
import com.nexmo.client.stitch.InAppConversationMemberRequest;
import com.nexmo.client.stitch.commons.AbstractMessagingMethod;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

/**
 * Created by Ergyun Syuleyman on 2/15/18.
 */

public class AddConversationMemberMethod extends AbstractMessagingMethod<InAppConversationMemberRequest, InAppConversationMemberEvent> {
    private static final Log LOG = LogFactory.getLog(AddConversationMemberMethod.class);

    private static final String DEFAULT_URI = "https://api.nexmo.com/beta/conversations/:id/members";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};
    private String uri = DEFAULT_URI;
    private String conversationId;

    public AddConversationMemberMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    public RequestBuilder makeRequest(InAppConversationMemberRequest request) throws NexmoClientException, UnsupportedEncodingException {
        if (request != null) {
            this.conversationId = request.getConversationId();
            URIBuilder uriBuilder;
            String finalUri = uri;
            if (conversationId != null) {
                finalUri = finalUri.replace(":id", conversationId);
            } else {
                throw new NexmoMethodFailedException("Bad request. Conversation ID is required!");
            }
            try {
                uriBuilder = new URIBuilder(finalUri);
            } catch (URISyntaxException e) {
                throw new NexmoUnexpectedException("Could not parse URI: " + this.uri);
            }

            LOG.debug("Application Conversation Members URL: " + uriBuilder.toString());

            return RequestBuilder.post()
                    .setUri(uriBuilder.toString())
                    .setHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(request.toJson()));
        }

        throw new NexmoMethodFailedException("Bad request. Request must not be NULL!");
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public InAppConversationMemberEvent parseResponse(HttpResponse response) throws IOException {
        String json;
        final StatusLine statusLine = response.getStatusLine();
        try {
            json = new BasicResponseHandler().handleResponse(response);
        } catch (HttpResponseException e) {
            json = "{}";
            LOG.error("Application Conversation Member add response: " + response.toString(), e);
        }
        InAppConversationMemberEvent conversationMemberEvent = InAppConversationMemberEvent.fromJson(json);
        conversationMemberEvent.setStatusCode(statusLine.getStatusCode());
        conversationMemberEvent.setReasonPhrase(statusLine.getReasonPhrase());
        conversationMemberEvent.setConversationId(this.conversationId);
        return conversationMemberEvent;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
