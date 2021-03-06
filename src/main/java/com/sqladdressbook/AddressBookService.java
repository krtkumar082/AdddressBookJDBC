package com.sqladdressbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookService {
	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}

	private static List<AddressBookData> addList;
	private AddressBookDBService addressBookDBService;

	public AddressBookService() {
		addressBookDBService = AddressBookDBService.getInstance();
	}

	public AddressBookService(List<AddressBookData> empList) {
		this.addList = addList;
	}

	public List<AddressBookData> readAddressBookData(IOService dbIo) {
		if (dbIo.equals(IOService.DB_IO)) {
			this.addList = new AddressBookDBService().readData();
		}
		return this.addList;
	}
	private AddressBookData getAddressBookData(String name) {
		for (AddressBookData data : addList) {
			if (data.first_name.equals(name)) {
				return data;
			}
		}
		return null;
	}
	
	public void updateContactsCity(String firstname, String city) {
		int result = addressBookDBService.updateAddressBookData_Using_PreparedStatement(firstname, city);
		if (result == 0)
			return;
		AddressBookData addBookData = this.getAddressBookData(firstname);
		if (addBookData != null)
			addBookData.city = city;
	}

	public boolean checkAddressBookDataInSyncWithDB(String fname, String city) {
		for (AddressBookData data : addList) {
			if (data.first_name.equals(fname)) {
				if (data.city.equals(city)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Map<String, Integer> readCountContactsByCity(IOService ioService) {
		if(ioService.equals(IOService.DB_IO)) {
			return addressBookDBService.getCountByCity();
		}
		return null;
	}

	public Map<String, Integer> readCountContactsByState(IOService ioService) {
		if(ioService.equals(IOService.DB_IO)) {
			return addressBookDBService.getCountByState();
		}
		return null;
	}
	
	public void addContact(String first_name, String last_name, String  address, String city, String state, String zipcode, String phone_no, String email) {
		addList.add(addressBookDBService.addContact(first_name, last_name, address, city, state, zipcode, phone_no, email));
	}

	public void addContactsWithThreads(List<AddressBookData> addBookList) {
		Map<Integer, Boolean> contactAdditionStatus = new HashMap<Integer, Boolean>();
		addBookList.forEach(ad -> {
			Runnable task = () -> {
				contactAdditionStatus.put(addressBookDBService.hashCode(), false);

				this.addContact(ad.first_name, ad.last_name, ad.address, ad.city, ad.state, ad.zip, ad.phone_no,
						ad.email);
				contactAdditionStatus.put(addressBookDBService.hashCode(), true);

			};
			Thread thread = new Thread(task, ad.first_name);
			thread.start();
		});
		while (contactAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}
}
