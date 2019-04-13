import controller.ManagerController;
import model.Cook;
import model.Dish;
import model.RestaurantDBLayer;
import model.actionresults.CookResponse;
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class TestManagerController {

    @Mock
    private static RestaurantDBLayer db = mock(RestaurantDBLayer.class);

    @InjectMocks
    private static ManagerController controller;

    private Dish testDish;
    private Cook testCook;

    @BeforeAll
    static void setupAll() {
        controller = ManagerController.getInstance();
        controller.setDb(db);
    }

    @BeforeEach
    void setupEach() {
        initMocks(this);
        resetTestDish();
    }

    private void resetTestDish() {
        testDish = Dish.builder()
                .id(1)
                .name("TestDish")
                .description("Desc")
                .price(55.5F)
                .timeToPrepare(5)
                .rate(4.2F)
                .rateCount(5)
                .imagePath("Path/To/Image.png")
                .build();

        testCook = new Cook(0, "F_NAME", "L_NAME");
    }

    @Test
    void addDishShouldFailWhenNameIsEmpty() {
        testDish.setName("");
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    void addDishShouldFailWhenNameIsNull() {
        testDish.setName(null);
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    void addDishShouldFailWhenDescriptionIsEmpty() {
        testDish.setDescription("");
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    void addDishShouldFailWhenDescriptionIsNull() {
        testDish.setDescription("");
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    void addDishShouldReturnNotSuccessWhenImageIsNull() {
        testDish.setImagePath(null);
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    void addDishShouldFailWhenTimeToPrepareLessThanZero() {
        testDish.setTimeToPrepare(-1);
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    void addDishShouldFailWhenPriceLessThanZero() {
        testDish.setPrice(-1);
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    void addDishShouldSucceed() throws Exception {
        doNothing().when(db).addDish(testDish);
        EmptyResponse response = controller.addDish(testDish);

        assertTrue(response.isSuccess());
        verify(db, times(1)).addDish(testDish);
    }

    @Test
    void addDishShouldFailWhenAddDishThrowsException() throws Exception {
        doThrow(new Exception()).when(db).addDish(testDish);
        EmptyResponse response = controller.addDish(testDish);

        assertFalse(response.isSuccess());
        verify(db, times(1)).addDish(testDish);
    }

    @Test
    void getDishesShouldFailWhenGetDishesThrowsException() throws Exception {
        doThrow(new Exception()).when(db).getAvailableDishes();
        DishResponse response = controller.getDishes();

        assertFalse(response.isSuccess());
        verify(db, times(1)).getAvailableDishes();
    }

    @Test
    void getDishesShouldReturnListOfDishes() throws Exception {
        when(db.getAvailableDishes()).thenReturn(Collections.singletonList(testDish));
        DishResponse response = controller.getDishes();

        assertTrue(response.isSuccess());
        verify(db, times(1)).getAvailableDishes();
        assertThat(response.getDishes(), Matchers.hasSize(1));
        assertThat(response.getDishes().get(0), Matchers.equalTo(testDish));
    }

    @Test
    void updateDishWithNegativeIdShouldFail() throws Exception {
        EmptyResponse response = controller.updateDish(-1, testDish);

        assertFalse(response.isSuccess());
        verify(db, times(0)).updateDish(-1, testDish);
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    void updateDishWithInvalidNewDishData() throws Exception {
        testDish.setName("");
        EmptyResponse response = controller.updateDish(0, testDish);
        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));

        resetTestDish();

        testDish.setDescription("");
        response = controller.updateDish(0, testDish);
        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));

        resetTestDish();

        testDish.setTimeToPrepare(-1);
        response = controller.updateDish(0, testDish);
        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    void updateDishShouldSucceed() throws Exception {
        doNothing().when(db).updateDish(0, testDish);
        EmptyResponse response = controller.updateDish(0, testDish);

        assertTrue(response.isSuccess());
        verify(db, times(1)).updateDish(0, testDish);
        assertThat(response.getMessage(), Matchers.blankOrNullString());
    }

    @Test
    void removeDishWithInvalidIdShouldFail() throws Exception {
        EmptyResponse response = controller.removeDish(-1);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
        verify(db, times(0)).removeDish(-1);
    }

    @Test
    void removeDishShouldSucceed() throws Exception {
        EmptyResponse response = controller.removeDish(0);

        assertTrue(response.isSuccess());
        assertThat(response.getMessage(), Matchers.blankOrNullString());
        verify(db, times(1)).removeDish(0);

    }

    @Test
    void getCooksShouldSucceed() throws Exception {
        when(db.getCooks()).thenReturn(Collections.singletonList(testCook));
        CookResponse response = controller.getCooks();

        assertTrue(response.isSuccess());
        assertThat(response.getMessage(), Matchers.blankOrNullString());
        verify(db, times(1)).getCooks();
    }

    @Test
    void addCookWithInvalidFName() {
        testCook.setFirstName(null);
        EmptyResponse response = controller.addCook(testCook);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    void addCookWithInvalidLName() {
        testCook.setLastName(null);
        EmptyResponse response = controller.addCook(testCook);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    void fireCookWithInvalidIdShouldFail() {
        EmptyResponse response = controller.fireCook(-1);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), Matchers.not(Matchers.blankOrNullString()));
    }

    @Test
    void fireCookShouldSucceed() throws Exception {
        doNothing().when(db).fireCook(0);
        EmptyResponse response = controller.fireCook(0);

        assertTrue(response.isSuccess());
        assertThat(response.getMessage(), Matchers.blankOrNullString());
        verify(db, times(1)).fireCook(0);
    }
}
