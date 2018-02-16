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

package com.nexmo.client.stitch.users;

import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.SerializationFeature;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.stitch.Constants;
import com.nexmo.client.stitch.InAppUserInfo;
import com.nexmo.client.stitch.InAppUserInfoPage;
import com.nexmo.client.stitch.InAppUsersFilter;
import com.nexmo.client.voice.endpoints.AbstractMethod;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by @authorErgyun Syuleyman on 2/13/18.
 */

public class ListUsersMethod extends AbstractMethod<InAppUsersFilter, InAppUserInfoPage> {
    private static final Log LOG = LogFactory.getLog(ListUsersMethod.class);

    private static final String DEFAULT_URI = "https://api.nexmo.com/beta/users";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};
    private String uri = DEFAULT_URI;

    public ListUsersMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(InAppUsersFilter filter) throws NexmoClientException, UnsupportedEncodingException {
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(this.uri);
        } catch (URISyntaxException e) {
            throw new NexmoUnexpectedException("Could not parse URI: " + this.uri);
        }
        if (filter != null) {
            List<NameValuePair> params = filter.toUrlParams();
            for (NameValuePair param : params) {
                uriBuilder.setParameter(param.getName(), param.getValue());
            }
        }
        return RequestBuilder.get().setUri(uriBuilder.toString());
    }

    @Override
    public InAppUserInfoPage parseResponse(HttpResponse response) throws IOException {
        String json;
        InAppUserInfoPage userInfoPage;
        final StatusLine statusLine = response.getStatusLine();
        try {
            json = new BasicResponseHandler().handleResponse(response);
        } catch (HttpResponseException e) {
            json = "{}";
            LOG.error("Application Users response: " + response.toString(), e);
        }

        LOG.debug("Application Users JSON: " + json );

        if (Constants.enableUsersListPagination) {
            userInfoPage = InAppUserInfoPage.fromJson(json);
        } else {
            EmbeddedInAppUsers embeddedInAppUsers = new EmbeddedInAppUsers();
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            embeddedInAppUsers.setInAppUserInfos(mapper.readValue(json, InAppUserInfo[].class));
            userInfoPage = new InAppUserInfoPage();
            userInfoPage.setEmbedded(embeddedInAppUsers);
        }
        userInfoPage.setStatusCode(statusLine.getStatusCode());
        userInfoPage.setReasonPhrase(statusLine.getReasonPhrase());
        return userInfoPage;
    }


    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return this.uri;
    }
}
