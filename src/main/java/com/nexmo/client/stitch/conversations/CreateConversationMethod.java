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
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.stitch.InAppConversation;
import com.nexmo.client.stitch.InAppConversationEvent;
import com.nexmo.client.stitch.commons.AbstractMessagingMethod;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Ergyun Syuleyman on 2/13/18.
 */

public class CreateConversationMethod extends AbstractMessagingMethod<InAppConversation, InAppConversationEvent> {
    private static final Log LOG = LogFactory.getLog(CreateConversationMethod.class);

    private static final String DEFAULT_URI = "https://api.nexmo.com/beta/conversations";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};
    private String uri = DEFAULT_URI;

    public CreateConversationMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    public RequestBuilder makeRequest(InAppConversation request) throws NexmoClientException, UnsupportedEncodingException {
        if (request != null) {
            return RequestBuilder.post(this.uri)
                    .setHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(request.toJson()));
        }

        return RequestBuilder.post(this.uri);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public InAppConversationEvent parseResponse(HttpResponse response) throws IOException {
        String json;
        final StatusLine statusLine = response.getStatusLine();
        try {
            json = new BasicResponseHandler().handleResponse(response);
        } catch (HttpResponseException e) {
            json = "{}";
            LOG.error("Application Conversation create response: " + response.toString(), e);
        }
        InAppConversationEvent conversationEvent = InAppConversationEvent.fromJson(json);
        conversationEvent.setStatusCode(statusLine.getStatusCode());
        conversationEvent.setReasonPhrase(statusLine.getReasonPhrase());
        return conversationEvent;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
