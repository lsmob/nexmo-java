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

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.stitch.InAppConversation;
import com.nexmo.client.stitch.InAppConversationEvent;
import com.nexmo.client.stitch.InAppConversationInfo;
import com.nexmo.client.stitch.InAppConversationInfoPage;
import com.nexmo.client.stitch.InAppConversationsFilter;
import com.nexmo.client.stitch.InAppMessagingClient;

import java.io.IOException;

/**
 * Created by Ergyun Syuleyman on 2/13/18.
 */

/**
 * Allows actions to be taken on <tt>/in-app messaging/*</tt> conversations.
 * <p>
 * <b>Note:</b> This is an internal object. All functionality is provided publicly by the {@link InAppMessagingClient} class.
 */

public class InAppConversations {
    private final CreateConversationMethod createConversation;
    private final ReadConversationMethod readConversation;
    private final ListConversationsMethod listConversations;


    public InAppConversations(HttpWrapper httpWrapper) {
        this.createConversation = new CreateConversationMethod(httpWrapper);
        this.readConversation = new ReadConversationMethod(httpWrapper);
        this.listConversations = new ListConversationsMethod(httpWrapper);
    }

    /**
     * Start a conversation configured by the provided {@link InAppConversation} object.
     * <p>
     * Requires a {@link com.nexmo.client.auth.JWTAuthMethod} to be provided to the NexmoClient which constructs
     *
     * @param request A Conversation filter configuring the conversation to be created
     * @return An InAppConversationEvent describing the conversation that was initiated.
     * @throws IOException          if an error occurs communicating with the Nexmo API
     * @throws NexmoClientException if an error occurs constructing the Nexmo API request or response
     */
    public InAppConversationEvent post(InAppConversation request) throws IOException, NexmoClientException {
        return this.createConversation.execute(request);
    }

    /**
     * List conversations which match the provided <tt>filter</tt>.
     * <p>
     * Requires a {@link com.nexmo.client.auth.JWTAuthMethod} to be provided to the NexmoClient which constructs
     *
     * @param filter An InAppConversationsFilter describing the conversations to be searched, or {@code null} for all conversations.
     * @return An InAppConversationInfoPage containing a single page of {@link InAppConversationInfo} results
     * @throws IOException          if an error occurs communicating with the Nexmo API
     * @throws NexmoClientException if an error occurs constructing the Nexmo API request or response
     */
    public InAppConversationInfoPage list(InAppConversationsFilter filter) throws IOException, NexmoClientException {
        return this.listConversations.execute(filter);
    }

    /**
     * Get details of a single conversation, identified by <tt>uuid</tt>.
     * <p>
     * Requires a {@link com.nexmo.client.auth.JWTAuthMethod} to be provided to the NexmoClient which constructs
     *
     * @param uuid The uuid of the InAppConversationInfo object to be retrieved
     * @return An InAppConversationInfo object describing the state of the conversation that was made or is in progress
     * @throws IOException          if an error occurs communicating with the Nexmo API
     * @throws NexmoClientException if an error occurs constructing the Nexmo API request or response
     */
    public InAppConversationInfo get(String uuid) throws IOException, NexmoClientException {
        return this.readConversation.execute(uuid);
    }
}
