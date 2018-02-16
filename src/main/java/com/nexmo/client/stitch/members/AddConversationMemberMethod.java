package com.nexmo.client.stitch.members;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoMethodFailedException;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.stitch.InAppConversationMemberEvent;
import com.nexmo.client.stitch.InAppConversationMemberRequest;
import com.nexmo.client.voice.endpoints.AbstractMethod;

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

        LOG.debug("Application Conversation Members URL: " + uriBuilder.toString());

        if (request != null) {
            return RequestBuilder.post()
                    .setUri(uriBuilder.toString())
                    .setHeader("Content-Type", "application/json")
                    .setEntity(new StringEntity(request.toJson()));
        }

        return RequestBuilder.post(uriBuilder.toString());
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
}
