package com.nexmo.client.stitch.conversations;/*
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

import com.nexmo.client.NexmoMethodFailedException;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.stitch.InAppConversationInfoPage;
import com.nexmo.client.stitch.InAppConversationsFilter;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ergyun Syuleyman on 2/18/18.
 */
public class ListConversationsMethodTest {
    ListConversationsMethod listConversationsMethod;

    @Before
    public void setUp() throws Exception {
        listConversationsMethod = new ListConversationsMethod(null);
        listConversationsMethod.setUri("https://api.nexmo.com/beta/{conversations}");
    }

    @Test
    public void makeRequest() throws Exception {
        RequestBuilder requestBuilder;
        InAppConversationsFilter filter = new InAppConversationsFilter();
        filter.setName("");
        filter.setOrder("ASC");
        filter.setPageSize(10);
        filter.setRecordIndex(3);
        try {
            requestBuilder = listConversationsMethod.makeRequest(filter);
        } catch (NexmoUnexpectedException ex) {
            assertTrue(ex.getMessage().startsWith("Could not parse URI"));
        }
        listConversationsMethod.setUri("https://api.nexmo.com/beta/conversations");
        try {
            requestBuilder = listConversationsMethod.makeRequest(filter);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(true);
        }

        requestBuilder = listConversationsMethod.makeRequest(null);

        assertEquals("GET", requestBuilder.getMethod());
        assertEquals("https://api.nexmo.com/beta/conversations", listConversationsMethod.getUri());
    }

    @Test
    public void parseResponse() throws Exception {
        InAppConversationInfoPage conversationInfoPage;
        HttpResponse errResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 400, "Bad Request")
        );
        conversationInfoPage = listConversationsMethod.parseResponse(errResponse);
        assertEquals("Bad Request", conversationInfoPage.getReasonPhrase());
        assertEquals(400, conversationInfoPage.getStatusCode());

        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "OK")
        );

        String json = "{" +
                "\"count\":1," +
                "\"page_size\":1," +
                "\"record_index\":0," +
                "\"_links\":{" +
                "   \"self\":{" +
                "       \"href\":\"http://conversation.local/v1/conversations\"}" +
                "   }," +
                "\"_embedded\":{" +
                "   \"conversations\":[" +
                "       {" +
                "           \"uuid\":\"CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2\"," +
                "           \"name\":\"NAM-c95a0cfc-ee80-441e-bbc3-fd61a99cd18f\"," +
                "           \"_links\":{" +
                "               \"self\":{" +
                "                   \"href\":\"http://conversation.local/v1/conversations/CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2\"" +
                "               }" +
                "           }" +
                "       }]" +
                "   }" +
                "}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        conversationInfoPage = listConversationsMethod.parseResponse(stubResponse);
        assertEquals(1, conversationInfoPage.getCount());
        assertEquals(1, conversationInfoPage.getPageSize());
        assertEquals(0, conversationInfoPage.getRecordIndex());
        assertEquals("CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2", conversationInfoPage.getEmbedded().getInAppConversationInfos()[0].getUuid());
        assertEquals("NAM-c95a0cfc-ee80-441e-bbc3-fd61a99cd18f", conversationInfoPage.getEmbedded().getInAppConversationInfos()[0].getName());
    }

}