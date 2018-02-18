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

import com.nexmo.client.NexmoMethodFailedException;
import com.nexmo.client.stitch.InAppConversationEvent;

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
public class CreateConversationMethodTest {
    CreateConversationMethod createConversationMethod;

    @Before
    public void setUp() throws Exception {
        createConversationMethod = new CreateConversationMethod(null);
    }

    @Test
    public void makeRequest() throws Exception {
        createConversationMethod.setUri("https://api.nexmo.com/beta/conversations");
        RequestBuilder requestBuilder;
        try {
            requestBuilder = createConversationMethod.makeRequest(null);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(true);
        }
    }

    @Test
    public void parseResponse() throws Exception {
        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 401, "Unauthorized")
        );

        String json = "{\n" +
                "  \"code\": \"system:error:invalid-token\",\n" +
                "  \"description\": \"the token was rejected\"\n" +
                "}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        InAppConversationEvent conversationEvent = createConversationMethod.parseResponse(stubResponse);
        assertEquals(401, conversationEvent.getStatusCode());
        assertEquals("Unauthorized", conversationEvent.getReasonPhrase());

        stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "Ok")
        );
        json = "{\"id\": \"CON-aaaaaaaa-bbbb-cccc-dddd-0123456789ab\", " +
                "\"href\": \"http://conversation.local/v1/conversations/CON-aaaaaaaa-bbbb-cccc-dddd-0123456789ab\" "+
                "}";
        jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        conversationEvent = createConversationMethod.parseResponse(stubResponse);
        assertEquals("CON-aaaaaaaa-bbbb-cccc-dddd-0123456789ab", conversationEvent.getId());
        assertEquals("http://conversation.local/v1/conversations/CON-aaaaaaaa-bbbb-cccc-dddd-0123456789ab", conversationEvent.getHref());
    }

}