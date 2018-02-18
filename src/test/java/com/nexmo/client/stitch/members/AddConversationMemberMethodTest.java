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

package com.nexmo.client.stitch.members;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexmo.client.NexmoMethodFailedException;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.stitch.commons.ChannelData;
import com.nexmo.client.stitch.InAppConversationMemberAction;
import com.nexmo.client.stitch.InAppConversationMemberEvent;
import com.nexmo.client.stitch.InAppConversationMemberRequest;
import com.nexmo.client.stitch.InAppConversationMemberState;

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
 * Created by Ergyun Syuleyman on 2/16/18.
 */
public class AddConversationMemberMethodTest {
    AddConversationMemberMethod addConversationMemberMethod;
    @Before
    public void setUp() throws Exception {
        addConversationMemberMethod = new AddConversationMemberMethod(null);
        addConversationMemberMethod.setConversationId("CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2");
    }

    @Test
    public void makeRequest() throws Exception {
        InAppConversationMemberRequest request = new InAppConversationMemberRequest("USR-98162fa4-0e30-4675-b066-30f13619c813",
                InAppConversationMemberAction.JOIN, new ChannelData("app"));
        addConversationMemberMethod.setUri("https://api.nexmo.com/beta/conversations/{:id}/members");
        RequestBuilder requestBuilder;
        try {
            requestBuilder = addConversationMemberMethod.makeRequest(request);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(true);
        }

        request.setConversationId("CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2");
        try {
            requestBuilder = addConversationMemberMethod.makeRequest(request);
        } catch (NexmoUnexpectedException ex) {
            assertTrue(ex.getMessage().startsWith("Could not parse URI"));
        }
        addConversationMemberMethod.setUri("https://api.nexmo.com/beta/conversations/:id/members");
        try {
            requestBuilder = addConversationMemberMethod.makeRequest(null);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(true);
        }

        requestBuilder = addConversationMemberMethod.makeRequest(request);

        assertEquals("POST", requestBuilder.getMethod());
        assertEquals("application/json", requestBuilder.getFirstHeader("Content-Type").getValue());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readValue(requestBuilder.getEntity().getContent(), JsonNode.class);
        assertEquals("USR-98162fa4-0e30-4675-b066-30f13619c813", node.get("user_id").asText());
        assertEquals("app", node.get("channel").get("type").asText());
        assertEquals("join", node.get("action").asText());
    }

    @Test
    public void parseResponse() throws Exception {
        InAppConversationMemberEvent memberEvent;
        HttpResponse errResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 400, "Bad Request")
        );
        memberEvent = addConversationMemberMethod.parseResponse(errResponse);
        assertEquals("Bad Request", memberEvent.getReasonPhrase());
        assertEquals(400, memberEvent.getStatusCode());

        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "OK")
        );

        String json = "{\n" +
                "  \"id\": \"MEM-08cd3743-7ca0-4113-b75d-b1c40467b3ae\",\n" +
                "  \"user_id\": \"USR-98162fa4-0e30-4675-b066-30f13619c813\",\n" +
                "  \"state\": \"JOINED\",\n" +
                "  \"timestamp\": {\n" +
                "    \"joined\": \"2020-01-01T12:00:00.000Z\"\n" +
                "  },\n" +
                "  \"channel\": {\n" +
                "    \"type\": \"app\"\n" +
                "  },\n" +
                "  \"href\": \"http://conversation.local/v1/conversations/CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2/MEM-08cd3743-7ca0-4113-b75d-b1c40467b3ae\"\n" +
                "}";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        memberEvent = addConversationMemberMethod.parseResponse(stubResponse);
        assertEquals("CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2", memberEvent.getConversationId());
        assertEquals("USR-98162fa4-0e30-4675-b066-30f13619c813", memberEvent.getUserId());
        assertEquals("MEM-08cd3743-7ca0-4113-b75d-b1c40467b3ae", memberEvent.getId());
        assertEquals("app", memberEvent.getChannel().getType());
        assertEquals("http://conversation.local/v1/conversations/CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2/MEM-08cd3743-7ca0-4113-b75d-b1c40467b3ae", memberEvent.getHref());
        assertEquals(InAppConversationMemberState.JOINED, memberEvent.getState());

        InAppConversationMemberEvent fromJson = InAppConversationMemberEvent.fromJson(json);
        System.out.println(fromJson.toString());
    }

    @Test
    public void setUri() throws Exception {
        addConversationMemberMethod.setUri("https://api.example.com/stitch/:id/members");
        InAppConversationMemberRequest request = new InAppConversationMemberRequest("USR-98162fa4-0e30-4675-b066-30f13619c813",
                InAppConversationMemberAction.JOIN, new ChannelData("app"));
        request.setConversationId("CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2");
        RequestBuilder requestBuilder = addConversationMemberMethod.makeRequest(request);
        assertEquals("https://api.example.com/stitch/CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2/members", requestBuilder.getUri().toString());
    }

}