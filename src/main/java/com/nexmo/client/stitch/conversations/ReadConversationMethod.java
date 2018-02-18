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

package com.nexmo.client.stitch.conversations;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoMethodFailedException;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.stitch.InAppConversationInfo;
import com.nexmo.client.stitch.commons.AbstractMessagingMethod;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

/**
 * Created by Ergyun Syuleyman on 2/13/18.
 */

public class ReadConversationMethod extends AbstractMessagingMethod<String, InAppConversationInfo> {
    private static final Log LOG = LogFactory.getLog(ReadConversationMethod.class);

    private static final String DEFAULT_BASE_URI = "https://api.nexmo.com/beta/conversations/";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};
    private String baseUri = DEFAULT_BASE_URI;

    public ReadConversationMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(String conversationId) throws NexmoClientException {
        if (conversationId != null && !conversationId.isEmpty()) {
            String uri = this.baseUri + conversationId;
            return RequestBuilder.get(uri);
        } else {
            throw new NexmoMethodFailedException("Bad request. Conversation ID is required!");
        }
    }

    @Override
    public InAppConversationInfo parseResponse(HttpResponse response) throws IOException {
        String json;
        final StatusLine statusLine = response.getStatusLine();
        try {
            json = new BasicResponseHandler().handleResponse(response);
        } catch (HttpResponseException e) {
            json = "{}";
            LOG.error("Application Conversation details response: " + response.toString(), e);
        }
        InAppConversationInfo conversationInfo = InAppConversationInfo.fromJson(json);
        conversationInfo.setStatusCode(statusLine.getStatusCode());
        conversationInfo.setReasonPhrase(statusLine.getReasonPhrase());
        return conversationInfo;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getBaseUri() {
        return baseUri;
    }
}