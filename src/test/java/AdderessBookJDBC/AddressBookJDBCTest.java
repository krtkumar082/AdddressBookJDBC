package AdderessBookJDBC;

import java.util.List;

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
}
