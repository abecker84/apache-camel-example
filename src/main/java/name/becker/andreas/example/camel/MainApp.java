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

import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example main class which bootstraps Apache Camel in order to process incoming
 * JSON files that are fired against the customer REST service, in order to
 * create new customers via the http PUT verb.
 */
public class MainApp {

	private static final Logger LOG = LoggerFactory.getLogger(MainApp.class);

	private static final JvmProperties PROPS = new JvmProperties();

	public static void main(String[] args) throws Exception {

		LOG.info("Bootstrapping Apache Camel...");

		// Create a Camel Context with a single route:

		CustomerServiceResponseHandler responseHandler = new CustomerServiceResponseHandlerLoggingImpl();
		CamelContext camelContext = CamelContextFactory.createCamelContext(PROPS, responseHandler);

		// Run the context - Would be nicer, if scheduled e.g. via Quartz
		// Another option would be utilizing a full-blown ESB solution, such as Fuse!

		camelContext.start();
		Thread.sleep(10000);
		camelContext.stop();

		LOG.info("Goodbye!");
	}
}
