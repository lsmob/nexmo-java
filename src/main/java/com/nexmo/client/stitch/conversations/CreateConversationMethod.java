package com.nexmo.client.stitch.conversations;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.stitch.InAppConversation;
import com.nexmo.client.stitch.InAppConversationEvent;
import com.nexmo.client.voice.endpoints.AbstractMethod;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Ergyun Syuleyman on 2/13/18.
 */

public class CreateConversationMethod extends AbstractMethod<InAppConversation, InAppConversationEvent> {
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
        String json = new BasicResponseHandler().handleResponse(response);
        return InAppConversationEvent.fromJson(json);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
