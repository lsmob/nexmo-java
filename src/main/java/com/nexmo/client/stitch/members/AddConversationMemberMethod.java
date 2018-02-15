package com.nexmo.client.stitch.members;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoMethodFailedException;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.stitch.InAppConversationEvent;
import com.nexmo.client.stitch.InAppConversationMemberEvent;
import com.nexmo.client.stitch.InAppConversationMemberRequest;
import com.nexmo.client.voice.endpoints.AbstractMethod;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
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

public class AddConversationMemberMethod extends AbstractMethod<InAppConversationMemberRequest, InAppConversationMemberEvent> {
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
        this.conversationId = request.getConversationId();
        URIBuilder uriBuilder;
        try {
            uriBuilder = new URIBuilder(this.uri);
        } catch (URISyntaxException e) {
            throw new NexmoUnexpectedException("Could not parse URI: " + this.uri);
        }
        if (conversationId != null) {
            uriBuilder.setParameter("id", conversationId);
        } else {
            throw new NexmoMethodFailedException("Not allowed request. Conversation ID is required!");
        }
        if (request != null) {
            return RequestBuilder.post()
                    .setUri(uriBuilder.toString())
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
    public InAppConversationMemberEvent parseResponse(HttpResponse response) throws IOException {
        String json = new BasicResponseHandler().handleResponse(response);
        return InAppConversationMemberEvent.fromJson(json);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
