package com.sqladdressbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
	private static AddressBookDBService abService;
	private PreparedStatement preparedStatement;
	public static AddressBookDBService getInstance() {
		if (abService == null) {
			abService = new AddressBookDBService();
		}
		return abService;
	}

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
		String userName = "root";
		String password = "Sushila@7838";
		Connection connection;
		connection = DriverManager.getConnection(jdbcURL, userName, password);
		return connection;
	}

	public List<AddressBookData> readData() {
		String sql = "select * from address_book_table;";
		List<AddressBookData> employeePayrollList = new ArrayList<>();
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			employeePayrollList = this.getAddressBookData(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private List<AddressBookData> getAddressBookData(ResultSet result) {
		List<AddressBookData> addressBookList = new ArrayList<>();
		try {
			while (result.next()) {
				
				String fname = result.getString("fname");
				String lname = result.getString("lname");
				String address = result.getString("Address");
				String city = result.getString("City");
				String state = result.getString("State");
				String zip = result.getString("Zip");
				String phone_no = result.getString("PhoneNumber");
				String email = result.getString("Email");
				addressBookList.add(new AddressBookData(fname, lname, address, city, state, zip, phone_no, email));
			}
			System.out.println(addressBookList);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return addressBookList;
	}
	
	public int updateAddressBookData_Using_PreparedStatement(String fname, String city) {
		return this.updateAddressBookDataUsingPreparedStatement(fname, city);
	}

	private int updateAddressBookDataUsingPreparedStatement(String fname, String city) {
		String sql = String.format("update address_book_table set City= '%s' where fname = '%s';", city, fname);
		try (Connection connection = this.getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(sql);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
