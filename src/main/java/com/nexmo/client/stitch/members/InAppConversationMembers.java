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

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.stitch.InAppConversationMember;
import com.nexmo.client.stitch.InAppConversationMemberEvent;
import com.nexmo.client.stitch.InAppConversationMemberRequest;
import com.nexmo.client.stitch.InAppConversationMembersPage;

import java.io.IOException;

/**
 * Created by Ergyun Syuleyman on 2/15/18.
 */

public class InAppConversationMembers {
    private final ListConversationMembersMethod listMembers;
    private final AddConversationMemberMethod addMember;

    public InAppConversationMembers(HttpWrapper httpWrapper) {
        this.listMembers = new ListConversationMembersMethod(httpWrapper);
        this.addMember = new AddConversationMemberMethod(httpWrapper);
    }


    /**
     * List conversation members by conversation ID <tt>uuid</tt>.
     * <p>
     * Requires a {@link com.nexmo.client.auth.JWTAuthMethod} to be provided to the NexmoClient which constructs
     *
     * @param conversationId The uuid of the conversation to retrieve all memberss.
     * @return An InAppConversationMembersPage containing a single page of {@link InAppConversationMember} results
     * @throws IOException          if an error occurs communicating with the Nexmo API
     * @throws NexmoClientException if an error occurs constructing the Nexmo API request or response
     */
    public InAppConversationMembersPage getMembers(String conversationId) throws IOException, NexmoClientException {
        return this.listMembers.execute(conversationId);
    }


    /**
     * Add conversation member configured by the provided {@link InAppConversationMemberRequest} object.
     * <p>
     * Requires a {@link com.nexmo.client.auth.JWTAuthMethod} to be provided to the NexmoClient which constructs
     *
     * @param request A Conversation member configuring the conversation and user to be added
     * @return An InAppConversationMemberEvent describing the conversation member that was added.
     * @throws IOException          if an error occurs communicating with the Nexmo API
     * @throws NexmoClientException if an error occurs constructing the Nexmo API request or response
     */
    public InAppConversationMemberEvent addMember(InAppConversationMemberRequest request) throws IOException, NexmoClientException {
        return this.addMember.execute(request);
    }
}
