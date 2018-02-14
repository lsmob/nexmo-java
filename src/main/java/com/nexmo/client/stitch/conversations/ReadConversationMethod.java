package com.nexmo.client.stitch.conversations;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.stitch.InAppConversationInfo;
import com.nexmo.client.voice.endpoints.AbstractMethod;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;

/**
 * Created by Ergyun Syuleyman on 2/13/18.
 */

public class ReadConversationMethod extends AbstractMethod<String, InAppConversationInfo> {
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
    public RequestBuilder makeRequest(String conversationId) {
        String uri = this.baseUri + conversationId;
        return RequestBuilder.get(uri);
    }

    @Override
    public InAppConversationInfo parseResponse(HttpResponse response) throws IOException {
        String json = new BasicResponseHandler().handleResponse(response);
        return InAppConversationInfo.fromJson(json);
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getBaseUri() {
        return baseUri;
    }
}