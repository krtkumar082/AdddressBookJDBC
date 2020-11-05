package com.sqladdressbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public Map<String, Integer> getCountByCity() {
		String sql = "SELECT City, COUNT(City) AS count_city FROM address_book_table GROUP BY City";
		Map<String, Integer> cityToContactsMap = new HashMap<>();
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				String city = result.getString("City");
				int count = result.getInt("count_city");
				cityToContactsMap.put(city, count);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return cityToContactsMap;
	}

	public Map<String, Integer> getCountByState() {
		String sql = "SELECT State, COUNT(State) AS count_state FROM address_book_table GROUP BY State";
		Map<String, Integer> stateToContactsMap = new HashMap<>();
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			while(result.next()) {
				String state = result.getString("State");
				int count = result.getInt("count_state");
				stateToContactsMap.put(state, count);
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return stateToContactsMap;
	}

	public AddressBookData addContact(String first_name, String last_name, String address, String city, String state,
			String zipcode, String phone_no, String email) {
		AddressBookData addBookData = null;
		String sql = String.format("INSERT INTO address_book_table(fname, lname, Address, City, State, Zip, PhoneNumber,Email) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", first_name, last_name, address, city, state, zipcode, phone_no, email);
		try(Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
			if(rowAffected == 1) {
				ResultSet result = statement.getGeneratedKeys();
				if(result.next()) {
					
					String fname = result.getString("fname");
					String lname = result.getString("lname");
					String address1 = result.getString("Address");
					String city1 = result.getString("City");
					String state1 = result.getString("State");
					String zip = result.getString("Zip");
					String phoneno = result.getString("PhoneNumber");
					String email1 = result.getString("Email");
					addBookData = new AddressBookData(fname, lname, address1, city1, state1, zip, phoneno, email1);
				}
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return addBookData;
	
		
	}
}
