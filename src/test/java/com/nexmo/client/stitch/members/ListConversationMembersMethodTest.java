package com.nexmo.client.stitch.members;/*
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
import com.nexmo.client.stitch.InAppConversationMembersPage;

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
 * Created by Ergyun Syuleyman on 2/17/18.
 */
public class ListConversationMembersMethodTest {
    ListConversationMembersMethod listConversationMembersMethod;

    @Before
    public void setUp() throws Exception {
        listConversationMembersMethod = new ListConversationMembersMethod(null);
        listConversationMembersMethod.setConversationId("CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2");
        listConversationMembersMethod.setUri("https://api.nexmo.com/beta/conversations/{:id}/members");
    }

    @Test
    public void makeRequest() throws Exception {
        RequestBuilder requestBuilder;
        try {
            requestBuilder = listConversationMembersMethod.makeRequest(null);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(true);
        }

        String conversationId = "CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2";
        try {
            requestBuilder = listConversationMembersMethod.makeRequest(conversationId);
        } catch (NexmoUnexpectedException ex) {
            assertTrue(ex.getMessage().startsWith("Could not parse URI"));
        }
        listConversationMembersMethod.setUri("https://api.nexmo.com/beta/conversations/:id/members");
        try {
            requestBuilder = listConversationMembersMethod.makeRequest(null);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(true);
        }

        requestBuilder = listConversationMembersMethod.makeRequest(conversationId);

        assertEquals("GET", requestBuilder.getMethod());
        assertEquals("CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2", listConversationMembersMethod.getConversationId());
        assertEquals("https://api.nexmo.com/beta/conversations/:id/members", listConversationMembersMethod.getUri());
    }

    @Test
    public void parseResponse() throws Exception {
        InAppConversationMembersPage memberPage;
        HttpResponse errResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 400, "Bad Request")
        );
        memberPage = listConversationMembersMethod.parseResponse(errResponse);
        assertEquals("Bad Request", memberPage.getReasonPhrase());
        assertEquals(400, memberPage.getStatusCode());

        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "OK")
        );

        String json = "[\n" +
                "  {\n" +
                "    \"user_id\": \"USR-aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" +
                "    \"name\": \"MEM-aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" +
                "    \"user_name\": \"Dillon\",\n" +
                "    \"state\": \"JOINED\"\n" +
                "  }\n" +
                "]";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        memberPage = listConversationMembersMethod.parseResponse(stubResponse);
        assertEquals("CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2", memberPage.getConversationId());
        assertEquals(1, memberPage.getCount());
        assertEquals(200, memberPage.getStatusCode());
        assertEquals("OK", memberPage.getReasonPhrase());
        assertEquals("USR-aaaaaaaa-bbbb-cccc-dddd-0123456789ab", memberPage.getEmbedded().getInAppConversationMembers()[0].getUserId());
        assertEquals("Dillon", memberPage.getEmbedded().getInAppConversationMembers()[0].getUserName());
        assertEquals("MEM-aaaaaaaa-bbbb-cccc-dddd-0123456789ab", memberPage.getEmbedded().getInAppConversationMembers()[0].getName());
        assertEquals("joined", memberPage.getEmbedded().getInAppConversationMembers()[0].getState().toString());

        String jsonMembers = "{\n" +
                "    \"count\":1,\n" +
                "    \"page_size\":1,\n" +
                "    \"record_index\":0,\n" +
                "    \"_links\":{\n" +
                "        \"self\":{\n" +
                "            \"href\":\"http://conversation.local/v1/conversations/CON-aaaaaaaa-bbbb-cccc-dddd-0123456789ab/members\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"_embedded\":{\n" +
                "        \"members\":[\n" +
                "            {\n" +
                "                \"id\": \"MEM-aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" +
                "                \"user_id\": \"USR-aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" +
                "                \"state\": \"JOINED\",\n" +
                "                \"timestamp\": {\n" +
                "                  \"joined\": \"2020-01-01T12:00:00.000Z\"\n" +
                "                },\n" +
                "                \"channel\": {\n" +
                "                  \"type\": \"app\"\n" +
                "                },\n" +
                "                \"_links\":{\n" +
                "                    \"self\":{\n" +
                "                        \"href\":\"http://conversation.local/v1/conversations/CON-aaaaaaaa-bbbb-cccc-dddd-0123456789ab/MEM-aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "\t]\n" +
                "   }\n" +
                "}\n";
        memberPage = InAppConversationMembersPage.fromJson(jsonMembers);
        assertEquals(0, memberPage.getRecordIndex());
        assertEquals(1, memberPage.getPageSize());
        System.out.println(memberPage.getEmbedded().getInAppConversationMembers()[0].toString());

    }

    @Test
    public void setUri() throws Exception {
        listConversationMembersMethod.setUri("https://api.example.com/stitch/:id/members");
        String conversationId = "CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2";
        RequestBuilder requestBuilder = listConversationMembersMethod.makeRequest(conversationId);
        assertEquals("https://api.example.com/stitch/CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2/members", requestBuilder.getUri().toString());
    }

}