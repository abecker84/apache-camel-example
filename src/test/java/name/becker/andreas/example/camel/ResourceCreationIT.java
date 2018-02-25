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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.CamelContext;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Integration tests for the camel route (inbox directory for JSON files -->
 * REST service) and the example REST service (PUT and GET methods).
 */
public class ResourceCreationIT {

	Logger LOG = LoggerFactory.getLogger(ResourceCreationIT.class);

	private String resourceUrl;

	/**
	 * Copies a simple example JSON file into the camel inbox directory, bootstraps
	 * the camel context (which should route the file to the REST service's PUT
	 * method) and calls the GET method of the REST service, in order to inspect the
	 * created resource.
	 */
	@Test
	public void testResourceCreation() throws Exception {

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Create camel context:
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		JvmProperties props = new JvmProperties();
		resourceUrl = null;

		CamelContext camelContext = CamelContextFactory.createCamelContext(props,
				new CustomerServiceResponseHandler() {
					@Override
					public void handlePutResponse(String locationHeader) {
						resourceUrl = locationHeader;
					}
				});

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Copy example file to inbox directory!
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		String inboxPath = props.getCamelExchangePath() + File.separator + "inbox";

		File outFile = new File(inboxPath + File.separator + "uli-upload.json");

		try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("uli-upload.json");
				OutputStream out = new FileOutputStream(outFile)) {
			// Max length per line = 1024.
			byte[] buffer = new byte[1024];

			int lineLength;
			while ((lineLength = in.read(buffer)) > 0) {
				out.write(buffer, 0, lineLength);
			}
		}

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Run the context:
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		camelContext.start();
		Thread.sleep(5000);
		camelContext.stop();

		// Now, camel should have routed the file to our example REST service!
		// Subsequently, a new resource should have been created in the service.
		// Our handler should have received the new resource's URL!

		Assert.assertNotNull(resourceUrl);

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Call returned resourceUrl and inspect the created resource:
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		Client client = ClientBuilder.newClient(new ClientConfig());
		WebTarget webTarget = client.target(resourceUrl);
		Response jsonResponse = webTarget.path("").request().accept(MediaType.APPLICATION_JSON).get();
		String jsonResponseString = jsonResponse.readEntity(String.class);

		LOG.info("Received JSON string: " + jsonResponseString);

		Assert.assertTrue("surename not found!", jsonResponseString.contains("Uli"));
		Assert.assertTrue("lastname not found!", jsonResponseString.contains("Upload"));
		Assert.assertTrue("email not found!", jsonResponseString.contains("uli.upload@upload.example"));
		Assert.assertTrue("dateOfBirth not found!", jsonResponseString.contains("1974-04-10"));

	}
}
