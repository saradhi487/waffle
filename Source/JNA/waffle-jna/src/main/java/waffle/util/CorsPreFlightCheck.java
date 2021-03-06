/**
 * Waffle (https://github.com/Waffle/waffle)
 *
 * Copyright (c) 2010-2020 Application Security, Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors: Application Security, Inc.
 */
package waffle.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class CorsPrefFlightCheck.
 */
public final class CorsPreFlightCheck {

    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CorsPreFlightCheck.class);

    /** The Constant preflightAttributeValue. */
    private static final String PRE_FLIGHT_ATTRIBUTE_VALUE = "PRE_FLIGHT";

    /** The Constant CORS_PRE_FLIGHT_HEADERS. */
    private static final List<String> CORS_PRE_FLIGHT_HEADERS = new ArrayList<String>() {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        {
            this.add("Access-Control-Request-Method");
            this.add("Access-Control-Request-Headers");
            this.add("Origin");
        }
    };

    /**
     * Prevent Instantiation.
     */
    private CorsPreFlightCheck() {
        // Do Nothing
    }

    /**
     * Checks if is preflight.
     *
     * @param request
     *            the request
     * @return true, if is preflight
     */
    public static boolean isPreflight(final HttpServletRequest request) {

        final String corsRequestType = (String) request.getAttribute("cors.request.type");

        CorsPreFlightCheck.LOGGER
                .debug("[waffle.util.CorsPreflightCheck] Request is CORS preflight; continue filter chain");

        // Method MUST be an OPTIONS Method to be a preflight Request
        final String method = request.getMethod();
        if (method == null || !method.equalsIgnoreCase("OPTIONS")) {
            return false;
        }

        CorsPreFlightCheck.LOGGER.debug("[waffle.util.CorsPreflightCheck] check for PRE_FLIGHT Attribute");

        /**
         * Support Apache CorsFilter which would already add the Attribute cors.request.type with a value "PRE_FLIGHT"
         **/
        if (corsRequestType != null
                && corsRequestType.equalsIgnoreCase(CorsPreFlightCheck.PRE_FLIGHT_ATTRIBUTE_VALUE)) {
            return true;
        } else {
            /*
             * it is OPTIONS and it is not an CorsFilter PRE_FLIGHT request make sure that the request contains all of
             * the CORS preflight Headers
             */
            CorsPreFlightCheck.LOGGER.debug("[waffle.util.CorsPreflightCheck] check headers");

            for (final String header : CorsPreFlightCheck.CORS_PRE_FLIGHT_HEADERS) {
                final String headerValue = request.getHeader(header);
                CorsPreFlightCheck.LOGGER.debug("[waffle.util.CorsPreflightCheck] {}", header);

                if (headerValue == null) {
                    /* one of the CORS pre-flight headers is missing */
                    return false;
                }
            }
            CorsPreFlightCheck.LOGGER.debug("[waffle.util.CorsPreflightCheck] is preflight");

            return true;
        }
    }
}
