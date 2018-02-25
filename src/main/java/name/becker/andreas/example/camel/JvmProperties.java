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

import java.util.Objects;

/**
 * Class which reads required jvm properties and exposes the property values via
 * the corresponding getter methods.
 */
public class JvmProperties {

	// Path to camel exchange directory, e.g. /home/foo/camel:
	private static final String PROPERTY_CAMEL_EXCHANGE_PATH = "camelExchangePath";

	// Endpoint url of customer REST service, e.g.
	// http://localhost:8080/rest-example:
	private static final String PROPERTY_CUSTOMER_REST_ENDPOINT = "customerRestEndpoint";

	private String camelExchangePath;
	private String customerRestEndpoint;

	public JvmProperties() {
		camelExchangePath = System.getProperties().getProperty(PROPERTY_CAMEL_EXCHANGE_PATH);
		customerRestEndpoint = System.getProperties().getProperty(PROPERTY_CUSTOMER_REST_ENDPOINT);

		Objects.requireNonNull(camelExchangePath, "Camel exchange path must not be empty!");
		Objects.requireNonNull(customerRestEndpoint, "Customer REST endpoint must not be empty!");
	}

	public String getCamelExchangePath() {
		return camelExchangePath;
	}

	public void setCamelExchangePath(String camelExchangePath) {
		this.camelExchangePath = camelExchangePath;
	}

	public String getCustomerRestEndpoint() {
		return customerRestEndpoint;
	}

	public void setCustomerRestEndpoint(String customerRestEndpoint) {
		this.customerRestEndpoint = customerRestEndpoint;
	}

}
