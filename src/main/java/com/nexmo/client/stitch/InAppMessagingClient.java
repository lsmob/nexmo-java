package com.nexmo.client.stitch;

import com.nexmo.client.AbstractClient;
import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.stitch.conversations.InAppConversations;
import com.nexmo.client.stitch.users.InAppUsers;

import java.io.IOException;

/**
 * Created by Ergyun Syuleyman on 2/13/18.
 */

/**
 * A client for talking to the Nexmo In-App Messaging API. The standard way to obtain an instance of this class is to use
 * {@link NexmoClient#getInAppMessagingClient()}.
 */
public class InAppMessagingClient extends AbstractClient {

    protected final InAppConversations conversations;
    protected final InAppUsers users;

    public InAppMessagingClient(HttpWrapper httpWrapper) {
        super(httpWrapper);

        conversations = new InAppConversations(httpWrapper);
        users = new InAppUsers(httpWrapper);
    }

    /**
     * Begin a conversation to an in-app messaging.
     *
     * @param request Describing the initial state of the conversation to be made,
     *               containing the <tt>display-name</tt> of your choosing to create a conversation.
     *               Also can contains the <tt>name</tt> optional conversation name, must be unique but will auto-generated if you do not provide it.
     * @return An InAppConversationEvent describing the initial state of the conversation, containing the <tt>uuid</tt> required to
     * interact with the In-App Messsaging conversation.
     * @throws IOException          if a network error occurred contacting the Nexmo In-App Messaging API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public InAppConversationEvent createConversation(InAppConversation request) throws IOException, NexmoClientException {
        return conversations.post(request);
    }

    /**
     * Obtain the first page of InAppConversationInfo objects, representing the most recent conversations initiated by
     * {@link #createConversation(InAppConversation)}.
     *
     * @return An InAppConversationInfoPage representing the response from the Nexmo In-App Messaging API.
     * @throws IOException          if a network error occurred contacting the Nexmo In-App Messaging API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public InAppConversationInfoPage listConversations() throws IOException, NexmoClientException {
        return this.listConversations(null);
    }

    /**
     * Obtain the first page of InAppConversationInfo objects matching the query described by <tt>filter</tt>, representing the most
     * recent conversations initiated by {@link #createConversation(InAppConversation)}.
     *
     * @param filter (optional) A filter describing which conversations to be listed.
     * @return An InAppConversationInfoPage representing the response from the Nexmo In-App Messaging API.
     * @throws IOException          if a network error occurred contacting the Nexmo In-App Messaging API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public InAppConversationInfoPage listConversations(InAppConversationsFilter filter) throws IOException, NexmoClientException {
        return conversations.get(filter);
    }

    /**
     * Look up the status of a single conversation initiated by {@link #createConversation(InAppConversation)}.
     *
     * @param uuid (required) The UUID of the conversation, obtained from the object returned by
     *             {@link #createConversation(InAppConversation)}.
     *             This value can be obtained with {@link InAppConversationEvent#getId()}
     * @return An InAppConversationInfo object, representing the response from the Nexmo In-App Messaging API.
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public InAppConversationInfo getConversationDetails(String uuid) throws IOException, NexmoClientException {
        return conversations.get(uuid);
    }


    /**
     * Begin an user to an in-app messaging.
     *
     * @param request Describing the initial state of the user to be made,
     *               containing the <tt>name</tt> of your choosing to create an user.
     * @return An InAppUserEvent describing the initial state of the user, containing the <tt>uuid</tt> required to
     * interact with the In-App Messsaging user.
     * @throws IOException          if a network error occurred contacting the Nexmo In-App Messaging API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public InAppUserEvent createUser(InAppUser request) throws IOException, NexmoClientException {
        return users.post(request);
    }

    /**
     * Obtain the first page of InAppUserInfo objects, representing the most recent Users initiated by
     * {@link #createUser(InAppUser)}.
     *
     * @return An InAppUserInfoPage representing the response from the Nexmo In-App Messaging API.
     * @throws IOException          if a network error occurred contacting the Nexmo In-App Messaging API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public InAppUserInfoPage listUsers() throws IOException, NexmoClientException {
        return this.listUsers(null);
    }

    /**
     * Obtain the first page of InAppUserInfo objects matching the query described by <tt>filter</tt>, representing the most
     * recent users initiated by {@link #createUser(InAppUser)}.
     *
     * @param filter (optional) A filter describing which users to be listed.
     * @return An InAppUserInfoPage representing the response from the Nexmo In-App Messaging API.
     * @throws IOException          if a network error occurred contacting the Nexmo In-App Messaging API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public InAppUserInfoPage listUsers(InAppUsersFilter filter) throws IOException, NexmoClientException {
        return users.get(filter);
    }

    /**
     * Look up the status of a single user initiated by {@link #createUser(InAppUser)}.
     *
     * @param uuid (required) The UUID of the user, obtained from the object returned by
     *             {@link #createUser(InAppUser)}.
     *             This value can be obtained with {@link InAppUserEvent#getId()}
     * @return An InAppUserInfo object, representing the response from the Nexmo In-App Messaging API.
     * @throws IOException          if a network error occurred contacting the Nexmo Voice API.
     * @throws NexmoClientException if there was a problem with the Nexmo request or response objects.
     */
    public InAppUserInfo getUserDetails(String uuid) throws IOException, NexmoClientException {
        return users.get(uuid);
    }

}
