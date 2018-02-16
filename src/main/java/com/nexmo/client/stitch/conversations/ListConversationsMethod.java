package com.nexmo.client.stitch.conversations;

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.stitch.Constants;
import com.nexmo.client.stitch.InAppConversationInfoPage;
import com.nexmo.client.stitch.InAppConversationsFilter;
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
 * Created by Ergyun Syuleyman on 2/13/18.
 */

public class ListConversationsMethod extends AbstractMethod<InAppConversationsFilter, InAppConversationInfoPage> {
    private static final Log LOG = LogFactory.getLog(ListConversationsMethod.class);

    private static final String DEFAULT_URI = "https://api.nexmo.com/beta/conversations";
    private static final Class[] ALLOWED_AUTH_METHODS = new Class[]{JWTAuthMethod.class};
    private String uri = DEFAULT_URI;

    public ListConversationsMethod(HttpWrapper httpWrapper) {
        super(httpWrapper);
    }

    @Override
    protected Class[] getAcceptableAuthMethods() {
        return ALLOWED_AUTH_METHODS;
    }

    @Override
    public RequestBuilder makeRequest(InAppConversationsFilter filter) throws NexmoClientException, UnsupportedEncodingException {
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
    public InAppConversationInfoPage parseResponse(HttpResponse response) throws IOException {
        String json;
        InAppConversationInfoPage conversationInfoPage;
        final StatusLine statusLine = response.getStatusLine();
        try {
            json = new BasicResponseHandler().handleResponse(response);
        } catch (HttpResponseException e) {
            json = "{}";
            LOG.error("Application Conversation create response: " + response.toString(), e);
        }

        LOG.debug("Application Conversations JSON: " + json );

        if (Constants.enableConversationsListPagination) {
            conversationInfoPage = InAppConversationInfoPage.fromJson(json);
        } else {
            LOG.warn("Application Conversations List implemented with pagination support only.");
            conversationInfoPage = new InAppConversationInfoPage();
        }
        conversationInfoPage.setStatusCode(statusLine.getStatusCode());
        conversationInfoPage.setReasonPhrase(statusLine.getReasonPhrase());
        return conversationInfoPage;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return this.uri;
    }
}
