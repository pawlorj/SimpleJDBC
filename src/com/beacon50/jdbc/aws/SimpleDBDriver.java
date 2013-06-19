package com.beacon50.jdbc.aws;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

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
	
	public Connection connect(String url, Properties info) throws SQLException {
		try {
			if (!url.startsWith("jdbc:simpledb")) {
				throw new SQLException("incorrect url");
			}
 
			// small fix for Amazon
			String[] urlParts = url.split("\\?");
			if (urlParts.length == 2) {
				String accessKey = "";
				String secretKey = "";
				url = urlParts[0];
				for (String param : urlParts[1].split("&")) {
					String[] keyValue = param.split("=");
					if (keyValue.length == 2) {
						if (keyValue[0].compareTo("user") == 0) {
							accessKey = keyValue[1];
						}
						if (keyValue[0].compareTo("password") == 0) {
							secretKey = keyValue[1];
						}
					}
				}
				if (!accessKey.isEmpty() && !secretKey.isEmpty()) {
					info = new Properties(info);
					info.setProperty("accessKey", accessKey);
					info.setProperty("secretKey", secretKey);
				}
			}
 
			//assume for now that if there is any proxy
			//information, then do it all...
			if (info.getProperty("proxyHost") != null && (!info.getProperty("proxyHost").equals(""))) {
				return new SimpleDBConnection( info.getProperty("accessKey"),
						info.getProperty("secretKey"), new SimpleDBProxy(info));
			} else {
				return new SimpleDBConnection(info.getProperty("accessKey"),
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
