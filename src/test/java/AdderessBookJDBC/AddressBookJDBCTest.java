package AdderessBookJDBC;

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
}
