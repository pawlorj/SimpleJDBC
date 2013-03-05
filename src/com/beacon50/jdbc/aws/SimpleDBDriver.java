package com.beacon50.jdbc.aws;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;
import java.awt.List;

/**
 *
 */
public class SimpleDBDriver implements Driver {

	final private Logger log = Logger.getLogger("com.beacon50.jdbc.aws.SimpleDBDriver");

	static {
		try {
			DriverManager.registerDriver(new SimpleDBDriver());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param url
	 * @param info
	 * @return
	 * @throws SQLException
	 */
	public Connection connect(String url, Properties info) throws SQLException {
		try {
			log.info("GETTING AWS SIMPLE DB CONNECTION");
			if (!url.startsWith("jdbc:simpledb")) {
				throw new SQLException("incorrect url");
			}

			//create a copy of properties and then
			//remove keys from url and add them to properties
			Properties prop = new Properties(info);
			url = extractKeys(url, prop);

			//assume for now that if there is any proxy
			//information, then do it all...
			if (prop.getProperty("proxyHost") != null && (!prop.getProperty("proxyHost").equals(""))) {
				return new SimpleDBConnection(prop.getProperty("accessKey"),
						prop.getProperty("secretKey"), new SimpleDBProxy(prop));
			} else {
				return new SimpleDBConnection(prop.getProperty("accessKey"),
						info.getProperty("secretKey"));
			}
		} catch (Exception e) {
			throw new SQLException("unable to connect");
		}
	}

	public boolean acceptsURL(String url) throws SQLException {
		if (!url.startsWith("jdbc:simpledb")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Extracts AWS keys from URL and save them to connection properties.
	 * @param url Full URL with query parameters.
	 * @param info Connection properties.
	 * @return URL without AWS keys.
	 */
	public String extractKeys(String url, Properties info) {
		int queryPos = url.lastIndexOf('?');
		if (queryPos == -1) {
			return url;
		}

		String[] params = url.substring(queryPos + 1).split("&");
		List leftParams = new List();
		for (int j = 0; j < params.length; j++) {
			String param = params[j];
			int equalSign = param.indexOf('=');
			if (equalSign == -1) {
				break;
			}

			String key = param.substring(0, equalSign);
			if (key.equals("accesskey")) {
				info.setProperty("accessKey", param.substring(equalSign + 1));
			} else if (key.equals("secretkey")) {
				info.setProperty("secretKey", param.substring(equalSign + 1));
			} else {
				leftParams.add(param);
			}
		}

		if (leftParams.getItemCount() > 0) {
			StringBuilder paramsBuilder =
					new StringBuilder(url.substring(0, queryPos + 1));
			for (String param : leftParams.getItems()) {
				if (paramsBuilder.length() > 0) {
					paramsBuilder.append('&');
				}
				paramsBuilder.append(param);
			}
			return paramsBuilder.toString();
		}

		return url.substring(0, queryPos);
	}

	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		return new DriverPropertyInfo[0];
	}

	public int getMajorVersion() {
		return 0;
	}

	public int getMinorVersion() {
		return 0;
	}

	public boolean jdbcCompliant() {
		return false;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
