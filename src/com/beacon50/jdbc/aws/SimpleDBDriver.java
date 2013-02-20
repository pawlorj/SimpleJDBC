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
				
			return new SimpleDBConnection("AKIAITCZNBYNWGSBNO2Q", "n+jBw3XI9cYqBvRQPyE2SHyIONa0acpyL2pW7eOg");
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
