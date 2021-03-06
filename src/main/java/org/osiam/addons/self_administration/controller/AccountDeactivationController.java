/*
 * Copyright (C) 2014 tarent AG
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.osiam.addons.self_administration.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.osiam.addons.self_administration.exception.OsiamException;
import org.osiam.addons.self_administration.util.SelfAdministrationHelper;
import org.osiam.client.exception.OsiamClientException;
import org.osiam.client.oauth.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * A controller providing an operation for account deactivation.
 */
@Controller
public class AccountDeactivationController {

    @Inject
    AccountManagementService accountManagementService;

    /**
     * Deactivates the user with the given ID.
     * 
     * @param authorization
     *        Authorization header with access token
     * @param userId
     *        the user ID
     * @return the resulting HTTP status code
     */
    @RequestMapping(value = "/deactivation/{userId}", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> deactivateUser(@RequestHeader("Authorization") final String authorization,
            @PathVariable final String userId, final HttpServletRequest request) {

        AccessToken accessToken = new AccessToken.Builder(SelfAdministrationHelper.extractAccessToken(authorization)).build();

        try {
            accountManagementService.deactivateUser(userId, accessToken);
        } catch (OsiamClientException | OsiamException | MailException e) {
            return accountManagementService.handleException(e);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
