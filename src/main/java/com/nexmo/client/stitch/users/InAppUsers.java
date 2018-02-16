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

import com.nexmo.client.HttpWrapper;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.stitch.InAppUser;
import com.nexmo.client.stitch.InAppUserEvent;
import com.nexmo.client.stitch.InAppUsersFilter;
import com.nexmo.client.stitch.InAppUserInfo;
import com.nexmo.client.stitch.InAppUserInfoPage;

import java.io.IOException;

/**
 * Created by Ergyun Syuleyman on 2/14/18.
 */

public class InAppUsers {
        private final CreateUserMethod createUser;
        private final ReadUserMethod readUser;
        private final ListUsersMethod listUsers;


        public InAppUsers(HttpWrapper httpWrapper) {
            this.createUser = new CreateUserMethod(httpWrapper);
            this.readUser = new ReadUserMethod(httpWrapper);
            this.listUsers = new ListUsersMethod(httpWrapper);
        }

        /**
         * Start an user configured by the provided {@link InAppUser} object.
         * <p>
         * Requires a {@link com.nexmo.client.auth.JWTAuthMethod} to be provided to the NexmoClient which constructs
         *
         * @param request An User request configuring the user to be created
         * @return An InAppUserEvent describing the user that was initiated.
         * @throws IOException          if an error occurs communicating with the Nexmo API
         * @throws NexmoClientException if an error occurs constructing the Nexmo API request or response
         */
        public InAppUserEvent post(InAppUser request) throws IOException, NexmoClientException {
            return this.createUser.execute(request);
        }

        /**
         * List users which match the provided <tt>filter</tt>.
         * <p>
         * Requires a {@link com.nexmo.client.auth.JWTAuthMethod} to be provided to the NexmoClient which constructs
         *
         * @param filter An InAppUsersFilter describing the users to be searched, or {@code null} for all users.
         * @return An InAppUserInfoPage containing a single page of {@link InAppUserInfo} results
         * @throws IOException          if an error occurs communicating with the Nexmo API
         * @throws NexmoClientException if an error occurs constructing the Nexmo API request or response
         */
        public InAppUserInfoPage get(InAppUsersFilter filter) throws IOException, NexmoClientException {
            return this.listUsers.execute(filter);
        }

        /**
         * Get details of a single user, identified by <tt>uuid</tt>.
         * <p>
         * Requires a {@link com.nexmo.client.auth.JWTAuthMethod} to be provided to the NexmoClient which constructs
         *
         * @param uuid The uuid of the InAppUserInfo object to be retrieved
         * @return An InAppUserInfo object describing the state of the user that was made or is in progress
         * @throws IOException          if an error occurs communicating with the Nexmo API
         * @throws NexmoClientException if an error occurs constructing the Nexmo API request or response
         */
        public InAppUserInfo get(String uuid) throws IOException, NexmoClientException {
            return this.readUser.execute(uuid);
        }
}
