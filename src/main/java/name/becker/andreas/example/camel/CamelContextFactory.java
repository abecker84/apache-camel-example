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

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple factory class to create Camel contexts.
 */
public class CamelContextFactory {

	private static final Logger LOG = LoggerFactory.getLogger(CamelContextFactory.class);

	public static CamelContext createCamelContext(JvmProperties props,
			CustomerServiceResponseHandler responseHandler) {
		// Create a Camel Context with a single route:
		CamelContext camelContext = new DefaultCamelContext();

		camelContext.setUseMDCLogging(Boolean.TRUE);

		// Incoming JSON files --> REST service:
		try {
			camelContext.addRoutes(new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from("file:" + props.getCamelExchangePath() + File.separator + "inbox?include=.*json$"). //
					log("Sending message: ${body}"). //
					setHeader(Exchange.CONTENT_TYPE, constant("application/json")). //
					setHeader(Exchange.HTTP_METHOD, constant("PUT")). //
					to(props.getCustomerRestEndpoint() + "/customers"). //
					process(new Processor() {
						@Override
						public void process(Exchange exchange) throws Exception {
							Message message = exchange.getIn();
							Integer responseCode = (Integer) message.getHeader(Exchange.HTTP_RESPONSE_CODE);
							if (responseCode.equals(201)) {
								// Created --> Process location header!
								String locationHeader = (String) message.getHeader("Location");
								if (locationHeader == null) {
									throw new RuntimeException("Did not receive location header!");
								}
								if (responseHandler != null) {
									responseHandler.handlePutResponse(locationHeader);
								}
							} else {
								// Something went wrong!
								throw new RuntimeException("Received status code " + responseCode.intValue());
							}
						}
					});
				}
			});
		} catch (Exception e) {
			throw new RuntimeException("Error while creating Camel context!", e);
		}

		return camelContext;
	}
}
