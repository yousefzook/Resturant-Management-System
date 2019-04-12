import model.Dish;
import model.RestaurantDBLayer;
import org.junit.Test;

import static org.junit.Assert.*;

public class RestaurantDBLayerTest {

    @Test
    public void testAddDish() throws Exception {

        RestaurantDBLayer dbLayer = new RestaurantDBLayer("RESTURANT.db");
        Dish dish = Dish.builder()
                .id(1)
                .name("dish1")
                .description("desc1")
                .price(15.2F)
                .rate(5)
                .rateCount(2)
                .image("abc123".getBytes())
                .build();
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
            dish.setId(2);
            dbLayer.addDish(dish);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            ex = e;
            e.printStackTrace();
        }
        assertNull(ex); // different id
    }

    private void createTEMP() {
        RestaurantDBLayer dbLayer = null;
        try {
            dbLayer = new RestaurantDBLayer("TEMP.db");
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Dish dish = Dish.builder()
                .id(1)
                .name("dish1")
                .description("desc1")
                .price(15.2F)
                .rate(5)
                .rateCount(2)
                .timeToPrepare(10)
                .image("abc123".getBytes())
                .build();

        try {
            assert dbLayer != null;
            dbLayer.addDish(dish);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            dish.setId(2);
            dbLayer.addDish(dish);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDishesUpdateRemoveReadd() throws Exception {

        createTEMP();
        RestaurantDBLayer dbLayer = new RestaurantDBLayer("TEMP.db");
        int[] ids = {1, 2, 3};
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
        assertEquals(1, dishes[0].getId());
        assertEquals(2, dishes[1].getId());

        //////////// update test
        Dish old = Dish.builder()
                .id(1)
                .name("dish1")
                .description("desc1")
                .price(15.2F)
                .rate(5)
                .rateCount(2)
                .timeToPrepare(10)
                .image("abc123".getBytes())
                .build();

        Dish newDish = Dish.builder()
                .id(1)
                .name("dishNew")
                .description("desc1")
                .price(15.2F)
                .rate(5)
                .rateCount(2)
                .timeToPrepare(10)
                .image("abc123".getBytes())
                .build();

        ex = null;
        try {
            dbLayer.updateDish(old.getId(), newDish);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            ex = e;
            e.printStackTrace();
        }
        assertNull(ex);

        ex = null;

        old = Dish.builder()
                .id(1)
                .name("dishNew")
                .description("desc1")
                .price(15.2F)
                .rate(5)
                .rateCount(2)
                .timeToPrepare(10)
                .image("abc123".getBytes())
                .build();

        newDish = Dish.builder()
                .id(1)
                .name("dishNew")
                .description("desc1")
                .price(3)
                .rate(5)
                .rateCount(2)
                .timeToPrepare(10)
                .image("abc123".getBytes())
                .build();
        try {
            dbLayer.updateDish(old.getId(), newDish);
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

}
