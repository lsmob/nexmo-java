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

package com.nexmo.client.stitch;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoMethodFailedException;
import com.nexmo.client.TestUtils;
import com.nexmo.client.auth.JWTAuthMethod;
import com.nexmo.client.stitch.commons.ChannelData;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Ergyun Syuleyman on 2/17/18.
 */
public class InAppMessagingClientTest {
    private TestUtils testUtils = new TestUtils();
    NexmoClient client;

    @Before
    public void setUp() throws Exception {
        byte[] privateKeyBytes = testUtils.loadKey("test/keys/application_key");
        client = new NexmoClient(new JWTAuthMethod("application-id", privateKeyBytes));
    }

    @Test
    public void testConversations() throws Exception {
        //create Conversations
        InAppConversation request = new InAppConversation();
        long millis = System.currentTimeMillis() % 1000;
        request.setDisplayName("CONV_NAME_" + String.valueOf(millis));
        InAppConversationEvent temConv = null;
        try {
            temConv = client.getInAppMessagingClient().createConversation(request);
            assertTrue(temConv != null);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(false);
        }

        try {
            InAppConversationEvent result = client.getInAppMessagingClient().createConversation(null);
            assertTrue(result != null);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(false);
        }

        // list Conversations
        InAppConversationsFilter filter = new InAppConversationsFilter();
        try {
            InAppConversationInfoPage result = client.getInAppMessagingClient().listConversations(filter);
            assertTrue(true);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(false);
        }

        try {
            InAppConversationInfoPage result = client.getInAppMessagingClient().listConversations();
            assertTrue(true);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(false);
        }

        // get Conversation Details
        InAppConversationInfo result = client.getInAppMessagingClient().getConversationDetails("FAKE_ID");
        assertTrue((result.getStatusCode() == 401) || (result.getStatusCode() == 404));
    }

    @Test
    public void testUser() throws Exception {
        // create User
        InAppUser request = new InAppUser();
        long millis = System.currentTimeMillis() % 1000;
        request.setName("USER_NAME_" + String.valueOf(millis));
        try {
            InAppUserEvent temUser = client.getInAppMessagingClient().createUser(request);
            assertTrue(temUser != null);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(false);
        }

        // list Users
        try {
            InAppUserInfoPage result = client.getInAppMessagingClient().listUsers();
            assertTrue(true);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(false);
        }


        // get User Details
        InAppUserInfo result = client.getInAppMessagingClient().getUserDetails("FAKE_ID");
        assertTrue((result.getStatusCode() == 401) || (result.getStatusCode() == 404));
    }

    @Test
    public void testConversationMembers() throws Exception {
        // list Conversation Members
        try {
            InAppConversationMembersPage result = client.getInAppMessagingClient().listConversationMembers("FAKE_CONV_ID");
            assertTrue(true);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(false);
        }

        //add Conversation Member
        try {
            InAppConversationMemberRequest request = new InAppConversationMemberRequest("FAKE_USER_ID",
                    InAppConversationMemberAction.JOIN,
                    new ChannelData("app"));
            request.setConversationId("FAKE_CONV_ID");
            InAppConversationMemberEvent result = client.getInAppMessagingClient().addConversationMember(request);
            assertTrue(true);
        } catch (NexmoMethodFailedException ex) {
            assertTrue(false);
        }
    }

}