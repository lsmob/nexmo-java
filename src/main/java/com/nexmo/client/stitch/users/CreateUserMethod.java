package com.nexmo.client.stitch.users;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.stitch.InAppUser;
import com.nexmo.client.stitch.InAppUserEvent;
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

public class CreateUserMethod extends AbstractMethod<InAppUser, InAppUserEvent> {
    private static final Log LOG = LogFactory.getLog(CreateUserMethod.class);

    private static final String DEFAULT_URI = "https://api.nexmo.com/beta/users";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};
    private String uri = DEFAULT_URI;

    public CreateUserMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    public RequestBuilder makeRequest(InAppUser request) throws NexmoClientException, UnsupportedEncodingException {
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
    public InAppUserEvent parseResponse(HttpResponse response) throws IOException {
        String json = new BasicResponseHandler().handleResponse(response);
        return InAppUserEvent.fromJson(json);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
