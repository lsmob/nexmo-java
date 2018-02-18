package com.nexmo.client.stitch.users;/*
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
import com.nexmo.client.stitch.InAppUserEvent;

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
public class CreateUserMethodTest {
    CreateUserMethod createUserMethod;

    @Before
    public void setUp() throws Exception {
        createUserMethod = new CreateUserMethod(null);
    }

    @Test
    public void makeRequest() throws Exception {
        createUserMethod.setUri("https://api.nexmo.com/beta/users");
        RequestBuilder requestBuilder;
        try {
            requestBuilder = createUserMethod.makeRequest(null);
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

        InAppUserEvent userEvent = createUserMethod.parseResponse(stubResponse);
        assertEquals(401, userEvent.getStatusCode());
        assertEquals("Unauthorized", userEvent.getReasonPhrase());

        stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "Ok")
        );
        json = "{\"id\": \"USR-aaaaaaaa-bbbb-cccc-dddd-0123456789ab\", " +
                "\"href\": \"http://conversation.local/v1/users/USR-aaaaaaaa-bbbb-cccc-dddd-0123456789ab\" "+
                "}";
        jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        userEvent = createUserMethod.parseResponse(stubResponse);
        assertEquals("USR-aaaaaaaa-bbbb-cccc-dddd-0123456789ab", userEvent.getId());
        assertEquals("http://conversation.local/v1/users/USR-aaaaaaaa-bbbb-cccc-dddd-0123456789ab", userEvent.getHref());
    }

}