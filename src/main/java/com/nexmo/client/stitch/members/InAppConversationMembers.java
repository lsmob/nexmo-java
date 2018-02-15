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
