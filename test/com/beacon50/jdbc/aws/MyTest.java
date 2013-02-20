package com.beacon50.jdbc.aws;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.beacon50.jdbc.aws.util.SimpleJDBCTestHelper;

public class MyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
        String qry = "select * from bob_users limit 10";
        Connection conn = SimpleJDBCTestHelper.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(qry);

        while (rs.next()) {
            String email = rs.getString("email");
            System.out.println(email);
        }
	}

}
