/*
 * Copyright 2018, Andreas Becker <andreas AT becker DOT name>
 * 
 * This file is part of The Apache Camel example.
 * 
 * The Apache Camel example is free software: you can redistribute
 * it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * The Apache Camel example is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details. You should have received a copy of the GNU
 * General Public License along with The Apache Camel example. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package name.becker.andreas.example.camel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles responses from the customer REST service by logging the responses.
 */
public class CustomerServiceResponseHandlerLoggingImpl implements CustomerServiceResponseHandler {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceResponseHandlerLoggingImpl.class);

	@Override
	public void handlePutResponse(String locationHeader) {
		LOG.info("Received location header of created resource: " + locationHeader);
	}

}
