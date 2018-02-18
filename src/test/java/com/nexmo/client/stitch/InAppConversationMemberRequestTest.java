package com.nexmo.client.stitch;/*
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

import com.nexmo.client.NexmoUnexpectedException;
import com.nexmo.client.stitch.commons.ChannelData;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ergyun Syuleyman on 2/18/18.
 */
public class InAppConversationMemberRequestTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testInAppConversationMemberRequest() throws Exception {
        InAppConversationMemberRequest memberRequest = new InAppConversationMemberRequest();
        memberRequest.setConversationId("CON-fb72ca0e-dddb-4ee9-be99-33fef81119a2");
        memberRequest.setUserId("USR-98162fa4-0e30-4675-b066-30f13619c813");
        memberRequest.setAction(InAppConversationMemberAction.fromString("JOIN"));
        memberRequest.setChannel(new ChannelData("app"));

        String json = memberRequest.toJson();
        InAppConversationMemberRequest jsonMember = InAppConversationMemberRequest.fromJson(json);
        assertEquals(jsonMember.getUserId(), memberRequest.getUserId());
        assertNotSame(jsonMember.getConversationId(), memberRequest.getConversationId());
        assertEquals(jsonMember.getAction(), memberRequest.getAction());
        assertEquals(jsonMember.getChannel().getType(), memberRequest.getChannel().getType());

        InAppConversationMemberRequest jsonFailedMember = InAppConversationMemberRequest.fromJson("{}");
        assertTrue(jsonFailedMember.getUserId()==null);
        try {
            jsonFailedMember.fromJson("[]");
            assertTrue(false);
        } catch (NexmoUnexpectedException ex) {
            assertTrue(true);
        }

    }

}