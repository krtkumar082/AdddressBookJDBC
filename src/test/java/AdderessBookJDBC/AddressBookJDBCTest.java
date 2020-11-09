package AdderessBookJDBC;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.sqladdressbook.AddressBookData;
import com.sqladdressbook.AddressBookService;
import com.sqladdressbook.AddressBookService.IOService;

public class AddressBookJDBCTest {
	@Test
    public void givenEmpPayrollDataInDB_ShouldMatchEmpCount() {
    	AddressBookService service = new AddressBookService();
    	List<AddressBookData> addList = service.readAddressBookData(IOService.DB_IO);
    	Assert.assertEquals(6, addList.size());
    }
	
	@Test 
    public void givenNewCity_WhenUpdated_shouldMatchWithDB() {
    	AddressBookService service = new AddressBookService();
    	service.readAddressBookData(IOService.DB_IO);
    	service.updateContactsCity("Keshav", "Jaipur");
    	boolean result = service.checkAddressBookDataInSyncWithDB("Keshav","Jaipur");
		Assert.assertTrue(result);
    }
	
	@Test
	public void givenContactsData_WhenCountByState_ShouldReturnProperValue() {
		AddressBookService service = new AddressBookService();
		service.readAddressBookData(IOService.DB_IO);
		Map<String, Integer> countContactsByState = service.readCountContactsByState(IOService.DB_IO);
		Assert.assertTrue(countContactsByState.get("Rajasthan").equals(6));
	}
	
	@Test
	public void givenNewContact_WhenAdded_ShouldSyncWithDB() {
		AddressBookService service = new AddressBookService();
		service.readAddressBookData(IOService.DB_IO);
		service.addContact("def", "ghi", "12345678 street", "gurgaon", "Hr", "3719331", "8888884588", "def@gmail.com");
		boolean result = service.checkAddressBookDataInSyncWithDB("def", "gurgaon");
		Assert.assertTrue(result);
	}
	@Test 
    public void given3Contacts_WhenAdded_ShouldMatchContactsCount() {
    	AddressBookData[] addBookData = {
    			new AddressBookData("jkl", "mno", "12 street","city1", "state1", "1902345", "7777777777", "jkl@gmail.com"),
    			new AddressBookData("pqr", "stu", "13 street","city2", "state2", "1233145", "7777777776", "pqr@gmail.com"),
    			new AddressBookData("vwx", "yz", "14 street","city3", "state3", "12341315", "7777777775", "vwx@gmail.com"),
    	};
    	AddressBookService addBookService = new AddressBookService();
    	addBookService.readAddressBookData(IOService.DB_IO);
    	Instant threadStart = Instant.now();
    	addBookService.addContactsWithThreads(Arrays.asList(addBookData));
    	Instant threadEnd = Instant.now();
    	System.out.println("Duration with thread : " + Duration.between(threadStart, threadEnd));
    	List<AddressBookData> addressBookData = addBookService.readAddressBookData(IOService.DB_IO);
    	System.out.println(addressBookData.size());
    	Assert.assertEquals(8, addressBookData.size());
    }
}
