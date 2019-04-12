import org.junit.Test;

import model.Cook;
import model.Dish;
import model.ResturantDBLayer;

import static org.junit.Assert.*;

import java.util.List;

public class ResturantDBLayerTest {

	@Test
	public void testAddDish() throws Exception {

		ResturantDBLayer dbLayer = new ResturantDBLayer("RESTURANT.db");
		Dish dish = new Dish(1, "dish1", "desc1", (float) 15.2, 5, 2, 10, "abc123".getBytes());
		Exception ex = null;
		try {
			dbLayer.addDish(dish);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			e.printStackTrace();
		}
		assertNull(ex);

		ex = null;
		try {
			dbLayer.addDish(dish);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			e.printStackTrace();
			dbLayer.closeConnection();
		}
		assertNotNull(ex); // same id

		ex = null;
		try {
			dish.id = 2;
			dbLayer.addDish(dish);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			e.printStackTrace();
		}
		assertNull(ex); // different id
	}

	public void createTEMP() {
		ResturantDBLayer dbLayer = null;
		try {
			dbLayer = new ResturantDBLayer("TEMP.db");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Dish dish = new Dish(1, "dish1", "desc1", (float) 15.2, 5, 2, 10, "abc123".getBytes());
		try {
			dbLayer.addDish(dish);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			dish.id = 2;
			dbLayer.addDish(dish);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetDishesUpdateRemoveReadd() throws Exception {

		createTEMP();
		ResturantDBLayer dbLayer = new ResturantDBLayer("TEMP.db");
		int[] ids = { 1, 2, 3 };
		Exception ex = null;
		Dish[] dishes = null;
		try {
			dishes = dbLayer.getDishes(ids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			e.printStackTrace();
		}
		assertNull(ex);
		assertEquals(2, dishes.length);
		assertEquals(1, dishes[0].id);
		assertEquals(2, dishes[1].id);

		//////////// update test

		ex = null;
		Dish newDish = new Dish(6, "dishNew", "desc1", (float) 3, 5, 2, 10, "abc123".getBytes());
		try {
			dbLayer.updateDish(1, newDish);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			e.printStackTrace();
		}
		assertNull(ex);

		/////////////// Remove
		try {
			dbLayer.removeDish(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			e.printStackTrace();
		}
		assertNull(ex);

		///////////////// get available
		List<Dish> avDish = null;
		try {
			avDish = dbLayer.getAvailableDishes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			System.out.println("--------------------------------------------");
			e.printStackTrace();
		}
		assertNull(ex);
		assertEquals(1, avDish.size());
		
		///////////////// get unavailable
		List<Dish> unAvDish = null;
		try {
			unAvDish = dbLayer.getAvailableDishes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			e.printStackTrace();
		}
		assertNull(ex);
		assertEquals(1, unAvDish.size());
		
		
		//////////////// ReAdd
		ex = null;
		try {
			dbLayer.reAddDish(1);
		} catch (Exception e) {
			ex = e;
			e.printStackTrace();
		}
		assertNull(ex);

	}

	@Test
	public void testAddCook() throws Exception {

		ResturantDBLayer dbLayer = new ResturantDBLayer("RESTURANT.db");
		Cook cook = new Cook(1, "yousef", "zook");
		Exception ex = null;
		try {
			dbLayer.addCook(cook);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			e.printStackTrace();
		}
		assertNull(ex);

		ex = null;
		try {
			dbLayer.addCook(cook);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			e.printStackTrace();
			dbLayer.closeConnection();
		}
		assertNotNull(ex); // same id

		ex = null;
		try {
			cook.id = 2;
			dbLayer.addCook(cook);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			e.printStackTrace();
		}
		assertNull(ex); // different id

		/////////////// Remove
		ex = null;
		try {
			dbLayer.fireCook(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			e.printStackTrace();
		}
		assertNull(ex);

		/////////////// rehire
		ex = null;
		try {
			dbLayer.reHireCook(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			e.printStackTrace();
		}
		assertNull(ex);

		
		/////////////// getCooks
		ex = null;
		List<Cook> cooks = null;
		try {
			 cooks = dbLayer.getCooks();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ex = e;
			System.out.println("******************************************");
			e.printStackTrace();
		}
		assertNull(ex);
		assertEquals(2, cooks.size());
	}

	

}
