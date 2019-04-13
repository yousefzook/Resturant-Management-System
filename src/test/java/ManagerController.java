import controller.ManagerController;
import model.Dish;
import model.RestaurantDBLayer;
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

//@RunWith(MockitoJUnitRunner.class)
class TestManagerController {

    private static ManagerController controller;

    @Mock
    private static RestaurantDBLayer db;

    private Dish testDish;

    @BeforeAll
    public static void setupAll() throws Exception {
        controller = ManagerController.getInstance();
        controller.setDb(db);
    }

    @BeforeEach
    public void setupEach() throws Exception {
        initMocks(this);
        testDish = Dish.builder()
                .id(1)
                .name("TestDish")
                .description("Desc")
                .price(55.5F)
                .timeToPrepare(5)
                .rate(4.2F)
                .rateCount(5)
                .imagePath("Path/To/Image.png")
                .image(new byte[]{})
                .build();
    }

    @Test
    public void addDishShouldFailWhenNameIsEmpty() {
        testDish.setName("");
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    public void addDishShouldFailWhenNameIsNull() {
        testDish.setName(null);
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    public void addDishShouldFailWhenDescriptionIsEmpty() {
        testDish.setDescription("");
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    public void addDishShouldFailWhenDescriptionIsNull() {
        testDish.setDescription("");
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    public void addDishShouldReturnNotSuccessWhenImageIsNull() {
        testDish.setImage(null);
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    public void addDishShouldFailWhenTimeToPrepareLessThanZero() {
        testDish.setTimeToPrepare(-1);
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    public void addDishShouldFailWhenPriceLessThanZero() {
        testDish.setPrice(-1);
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    public void addDishShouldSucceed() throws Exception {
        doNothing().when(db).addDish(testDish);
        EmptyResponse response = controller.addDish(testDish);

        assertTrue(response.isSuccess());
        verify(db, times(1)).addDish(testDish);
    }

    @Test
    public void addDishShouldFailWhenAddDishThrowsException() throws Exception {
        doThrow(new Exception()).when(db).addDish(testDish);
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        verify(db, times(1)).addDish(testDish);
    }

    @Test
    public void getDishesShouldFailWhenGetDishesThrowsException() throws Exception {
        doThrow(new Exception()).when(db).getDishes(null);
        DishResponse response = controller.getDishes();

        assertFalse(response.isSuccess());
        verify(db, times(1)).getDishes(null);
    }

    @Test
    public void getDishesShouldReturnListOfDishes() throws Exception {
        when(db.getDishes(null)).thenReturn(new Dish[]{testDish});
        DishResponse response = controller.getDishes();

        assertTrue(response.isSuccess());
        verify(db, times(1)).getDishes(null);
        assertThat(response.getDishes(), Matchers.hasSize(1));
        assertThat(response.getDishes().get(0), Matchers.equalTo(testDish));
    }


}
