package kry.martin.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsonServiceStoreTest {
	
	private JsonServiceStore store;
	
	@Before
	public void init() {
		store = new JsonServiceStore("test.json");
	}
	
	@After
	public void down() {
		store.deleteStore();
	}
	
	@Test
	public void testCreateService() {
		int startAmount = store.getAllServices().size();
		Service s = new Service("first", "http://first.se", "OK", 0L);
		s.setId(23);
		store.createService(s);
		assert(store.getAllServices().size() == startAmount+1);
	}
	
	@Test
	public void testGetService() {
		int id = 747;
		Service s = new Service("myname", "http://my.name", "OK", 55L);
		s.setId(id);
		
		store.createService(s);
		Service s2 = store.getService(id);
		assert(s.getId() == s2.getId());
		assert(s.getLastChecked() == s2.getLastChecked());
		assert(s.getName().equals(s2.getName()));
		assert(s.getStatus().equals(s2.getStatus()));
		assert(s.getUrl().equals(s2.getUrl()));
	}
	
	@Test
	public void testRemoveService() {
		store.createService(new Service("", "", "", 0L));
		Service s = store.getAllServices().get(0);
		int amount = store.getAllServices().size();
		store.removeService(s.getId());
		assert(amount-1 == store.getAllServices().size());
	}
}
