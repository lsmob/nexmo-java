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

package com.nexmo.client.stitch.users;

import com.nexmo.client.NexmoMethodFailedException;
import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.stitch.InAppUserInfoPage;
import com.nexmo.client.stitch.InAppUsersFilter;

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
public class ListUsersMethodTest {
    ListUsersMethod listUsersMethod;

    @Before
    public void setUp() throws Exception {
        listUsersMethod = new ListUsersMethod(null);
        listUsersMethod.setUri("https://api.nexmo.com/beta/{users}");

    }

    @Test
    public void makeRequest() throws Exception {
        RequestBuilder requestBuilder;
        InAppUsersFilter filter = new InAppUsersFilter();
        filter.setName("");
        filter.setOrder("ASC");
        filter.setPageSize(10);
        filter.setRecordIndex(3);
        try {
            requestBuilder = listUsersMethod.makeRequest(filter);
        } catch (NexmoUnexpectedException ex) {
            assertTrue(ex.getMessage().startsWith("Could not parse URI"));
        }
        listUsersMethod.setUri("https://api.nexmo.com/beta/users");
        try {
            requestBuilder = listUsersMethod.makeRequest(filter);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(true);
        }

        requestBuilder = listUsersMethod.makeRequest(null);

        assertEquals("GET", requestBuilder.getMethod());
        assertEquals("https://api.nexmo.com/beta/users", listUsersMethod.getUri());
    }

    @Test
    public void parseResponse() throws Exception {
        InAppUserInfoPage userPage;
        HttpResponse errResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 400, "Bad Request")
        );
        userPage = listUsersMethod.parseResponse(errResponse);
        assertEquals("Bad Request", userPage.getReasonPhrase());
        assertEquals(400, userPage.getStatusCode());

        HttpResponse stubResponse = new BasicHttpResponse(
                new BasicStatusLine(new ProtocolVersion("1.1", 1, 1), 200, "OK")
        );

        String json = "[\n" +
                "  {\n" +
                "    \"name\": \"Dillon\",\n" +
                "    \"id\": \"USR-aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" +
                "    \"href\": \"http://conversation.local/v1/users/USR-aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n" +
                "  }\n" +
                "]";
        InputStream jsonStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(jsonStream);
        stubResponse.setEntity(entity);

        userPage = listUsersMethod.parseResponse(stubResponse);
        assertEquals(1, userPage.getCount());
        assertEquals(1, userPage.getPageSize());
        assertEquals(0, userPage.getRecordIndex());
        assertEquals("USR-aaaaaaaa-bbbb-cccc-dddd-0123456789ab", userPage.getEmbedded().getInAppUserInfos()[0].getId());
        assertEquals("Dillon", userPage.getEmbedded().getInAppUserInfos()[0].getName());
        assertEquals("http://conversation.local/v1/users/USR-aaaaaaaa-bbbb-cccc-dddd-0123456789ab", userPage.getEmbedded().getInAppUserInfos()[0].getHRef());

        String jsonList = "{\n" +
                "\n" +
                "    \"count\":1,\n" +
                "    \"page_size\":1,\n" +
                "    \"record_index\":0,\n" +
                "    \"_links\":{\n" +
                "        \"self\":{\n" +
                "            \"href\":\"http://conversation.local/v1/users\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"_embedded\":{\n" +
                "        \"users\":[\n" +
                "            {\n" +
                "                \"id\": \"USR-aaaaaaaa-bbbb-cccc-dddd-0123456789ab\",\n" +
                "                \"name\": \"Dillon\",\n" +
                "                \"_links\":{\n" +
                "                    \"self\":{\n" +
                "                        \"href\":\"http://conversation.local/v1/users/USR-aaaaaaaa-bbbb-cccc-dddd-0123456789ab\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "\t]\n" +
                "   }\n" +
                "}";
        userPage = InAppUserInfoPage.fromJson(jsonList);
        System.out.println(userPage.getEmbedded().getInAppUserInfos()[0].toString());
    }

}