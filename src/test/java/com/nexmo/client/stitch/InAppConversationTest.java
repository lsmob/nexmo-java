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

import com.nexmo.client.NexmoUnexpectedException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ergyun Syuleyman on 2/18/18.
 */
public class InAppConversationTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testInAppConversation() throws Exception {
        InAppConversation conv = new InAppConversation("Conversation Display Name");
        conv.setName("CONV_UNIQUE_NAME_XXXXXXXXXX");
        String json = conv.toJson();
        InAppConversation jsonConv = InAppConversation.fromJson(json);
        assertEquals(jsonConv.getName(), conv.getName());

        InAppConversation jsonFailedUser = InAppConversation.fromJson("{}");
        assertTrue(jsonFailedUser.getName()==null);
        try {
            InAppConversation.fromJson("[]");
            assertTrue(false);
        } catch (NexmoUnexpectedException ex) {
            assertTrue(true);
        }
        try {
            InAppConversation conv2 = new InAppConversation("CONV_UNIQUE_NAME_YYYYYYYYYYYY", "Conversation Display Name");
            conv2.toJson();
        } catch (NexmoUnexpectedException ex) {
            assertTrue(true);
        }
    }

}