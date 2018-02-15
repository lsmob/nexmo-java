package com.nexmo.client.stitch.members;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoMethodFailedException;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.stitch.InAppConversationMembersPage;
import com.nexmo.client.voice.endpoints.AbstractMethod;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
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
        return RequestBuilder.get().setUri(uriBuilder.toString());
    }

    @Override
    public InAppConversationMembersPage parseResponse(HttpResponse response) throws IOException {
        String json = new BasicResponseHandler().handleResponse(response);
        InAppConversationMembersPage conversationMembersPage = InAppConversationMembersPage.fromJson(json);
        conversationMembersPage.setConversationId(this.conversationId);
        return conversationMembersPage;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return this.uri;
    }
}
