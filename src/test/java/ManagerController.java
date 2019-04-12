import controller.ManagerController;
import model.DBMethods;
import model.Dish;
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;
import org.hamcrest.Matchers;
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

@RunWith(MockitoJUnitRunner.class)
class TestManagerController {

    private ManagerController controller;
    private Dish testDish;

    @Mock
    private DBMethods db;

    @BeforeEach
    public void setup() {
        initMocks(DBMethods.class);
        controller = new ManagerController();
        controller.setDb(db);

        testDish = new Dish(1, 5, 4,
                "TestDish", "Desc",
                4, 55, new byte[]{});
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
        EmptyResponse response = controller.addDish(testDish);
        doNothing().when(db).addDish(testDish);

        assertTrue(response.isSuccess());
        verify(db, times(1)).addDish(testDish);
    }

    @Test
    public void addDishShouldFailWhenAddDishThrowsException() throws Exception {
        EmptyResponse response = controller.addDish(testDish);
        doThrow(new Exception()).when(db).addDish(testDish);

        assertFalse(response.isSuccess());
        verify(db, times(1)).addDish(testDish);
    }

    @Test
    public void getDishesShouldFailWhenGetDishesThrowsException() throws Exception {
        DishResponse response = controller.getDishes();
        doThrow(new Exception()).when(db).getDishes(null);

        assertFalse(response.isSuccess());
        verify(db, times(1)).getDishes(null);
    }

    @Test
    public void getDishesShouldReturnListOfDishes() throws Exception {
        DishResponse response = controller.getDishes();
        doReturn(new Dish[]{testDish}).when(db).getDishes(new int[]{1});

        assertTrue(response.isSuccess());
        verify(db, times(1)).getDishes(new int[]{1});
        assertThat(response.getDishes(), Matchers.hasSize(1));
        assertThat(response.getDishes().get(0), Matchers.equalTo(testDish));
    }


}
