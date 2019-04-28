package controller;

import com.uploadcare.upload.UploadFailureException;
import main.manager.ManagerApp;
import model.actionresults.CookResponse;
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;
import model.actionresults.NumericResponse;
import model.entity.Cook;
import model.entity.Dish;
import model.repository.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ManagerApp.class)
class ManagerControllerTest {

    @Mock
    private CookRepository cookRepo;

    @Mock
    private DishRepository dishRepo;

    @Mock
    private OrderRepository orderRepo;

    @Mock
    private TableRepository tableRepo;

    @Mock
    private UploadCareService uploadCareService;

    @Mock
    private TransactionRepository transactionsRepo;

    @InjectMocks
    private ManagerController managerController;

    private Dish testDish;
    private Cook testCook;

    @BeforeEach
    void setupEach() {
        testDish = Dish.builder()
                .id(1)
                .name("TestDish")
                .description("Desc")
                .price(55.5F)
                .timeToPrepare(5)
                .rate(4.2F)
                .rateCount(5)
                .active(true)
                .imagePath("Path/To/Image.png")
                .build();

        testCook = new Cook(0, "F_NAME", "L_NAME", true);
    }


    @DisplayName("Test addDish with empty name")
    @Test
    void addDishWithEmptyName() {
        testDish.setName("");
        EmptyResponse response = managerController.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test add dish with null name")
    @Test
    void addDishWithNullName() {
        testDish.setName(null);
        EmptyResponse response = managerController.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test addDish with empty description")
    @Test
    void addDishWithEmptyDescription() {
        testDish.setDescription("");
        EmptyResponse response = managerController.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test addDish with null description")
    @Test
    void addDishWithNullDescription() {
        testDish.setDescription("");
        EmptyResponse response = managerController.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test addDish with null imagePath")
    @Test
    void addDishWithNullImagePath() {
        testDish.setImagePath(null);
        EmptyResponse response = managerController.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test addDish with invalid time-to-prepare")
    @Test
    void addDishWithNegativeTimeToPrepare() {
        testDish.setTimeToPrepare(-1);
        EmptyResponse response = managerController.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }


    @DisplayName("Test addDish with invalid price")
    @Test
    void addDishWithNegativePrice() {
        testDish.setPrice(-1F);
        EmptyResponse response = managerController.addDish(testDish);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }


    @DisplayName("Test addDish. Should trigger the mock")
    @Test
    void addDishNormalCase() throws UploadFailureException {
        String localImagePath = testDish.getImagePath();
        when(uploadCareService.saveImageToCloud(testDish.getImagePath())).thenReturn("UUID");
        when(dishRepo.save(testDish)).thenReturn(testDish);
        EmptyResponse response = managerController.addDish(testDish);

        assertTrue(response.isSuccess());
        verify(dishRepo, times(1)).save(testDish);
        verify(uploadCareService, times(1)).saveImageToCloud(localImagePath);
    }

    @DisplayName("Test getDishes. Should trigger the mock")
    @Test
    void getDishesNormalCase() {
        when(dishRepo.findAllByActive(true)).thenReturn(Collections.singletonList(testDish));
        DishResponse response = managerController.getDishes();

        assertTrue(response.isSuccess());
        verify(dishRepo, times(1)).findAllByActive(true);
        assertThat(response.getDishes(), Matchers.hasSize(1));
        assertThat(response.getDishes().get(0), Matchers.equalTo(testDish));
    }

    @DisplayName("Test updateDish with invalid id")
    @Test
    void updateDishWithNegativeId() {
        EmptyResponse response = managerController.updateDish(-1, testDish);

        assertFalse(response.isSuccess());
        verify(dishRepo, times(0)).save(isA(Dish.class));
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test updateDish with invalid newDish")
    @Test
    void updateDishWithInvalidNewDishData() {
        testDish.setPrice(-1F);
        EmptyResponse response = managerController.updateDish(0, testDish);
        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));

        setupEach();

        testDish.setTimeToPrepare(-1);
        response = managerController.updateDish(0, testDish);
        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test updateDish dish with wrong id. Should trigger findById")
    @Test
    void updateDishWithWrongId() {
        Dish oldDish = testDish;
        testDish = Dish.builder().name("New NAME").build();

        when(dishRepo.findById(oldDish.getId())).thenReturn(Optional.empty());
        EmptyResponse response = managerController.updateDish(oldDish.getId(), testDish);

        assertFalse(response.isSuccess());
        verify(dishRepo, times(1)).findById(oldDish.getId());
    }

    @DisplayName("Test updateDish. Should trigger save")
    @Test
    void updateDishNormalCase() {
        Dish oldDish = testDish;
        Dish newDish = Dish.builder().name("New NAME").build();

        when(dishRepo.findById(oldDish.getId())).thenReturn(Optional.of(oldDish));
        EmptyResponse response = managerController.updateDish(oldDish.getId(), newDish);

        assertTrue(response.isSuccess());
        verify(dishRepo, times(1)).findById(oldDish.getId());
        oldDish.setName("New NAME");
        verify(dishRepo, times(1)).save(oldDish);
        verify(dishRepo, times(1)).save(newDish);
    }

    @DisplayName("Test removeDish with invalid ID")
    @Test
    void removeDishWithNegativeId() {
        EmptyResponse response = managerController.removeDish(-1);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
        verify(dishRepo, times(0)).save(isA(Dish.class));
    }

    @DisplayName("Test removeDish. should trigger save")
    @Test
    void removeDishNormalCase() {
        when(dishRepo.findById(testDish.getId())).thenReturn(Optional.of(testDish));
        when(dishRepo.save(testDish)).thenReturn(testDish);
        EmptyResponse response = managerController.removeDish(testDish.getId());

        assertTrue(response.isSuccess());
        verify(dishRepo, times(1)).findById(testDish.getId());
        assertFalse(testDish.isActive());
        verify(dishRepo, times(1)).save(testDish);

    }

    @DisplayName("Test getCooks. should trigger mock")
    @Test
    void getCooksNormalCase() {
        when(cookRepo.getAllHiredWithoutOrders()).thenReturn(Collections.singletonList(testCook));
        CookResponse response = managerController.getHiredCooks();

        assertTrue(response.isSuccess());
        assertThat(response.getMessage(), isEmptyOrNullString());
        verify(cookRepo, times(1)).getAllHiredWithoutOrders();
        verify(cookRepo, times(0)).getAllWithoutOrders();
    }

    @DisplayName("Test addCook with invalid first name")
    @Test
    void addCookWithInvalidFName() {
        testCook.setFirstName(null);
        EmptyResponse response = managerController.addCook(testCook);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test addCook with invalid last name")
    @Test
    void addCookWithInvalidLName() {
        testCook.setLastName(null);
        EmptyResponse response = managerController.addCook(testCook);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test fire cook with invalid Id")
    @Test
    void fireCookWithNegativeId() {
        EmptyResponse response = managerController.fireCook(-1);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test fire. Should trigger mock")
    @Test
    void fireCookNormalCase() {
        when(cookRepo.findById(testCook.getId())).thenReturn(Optional.of(testCook));
        when(cookRepo.save(testCook)).thenReturn(testCook);
        EmptyResponse response = managerController.fireCook(testCook.getId());

        assertTrue(response.isSuccess());
        assertThat(response.getMessage(), isEmptyOrNullString());
        verify(cookRepo, times(1)).findById(testCook.getId());
        verify(cookRepo, times(1)).save(testCook);
    }

    @DisplayName("Test getTotalIncome with reversed start and end dates")
    @Test
    void getTotalIncomeWithReversedDateShouldFail() {
        Date startDate = new Date(1318386508009L);
        Date endDate = new Date(1318386508000L);
        NumericResponse response = managerController.getTotalIncome(startDate, endDate);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test getTotalIncome. should trigger mock")
    @Test
    void getTotalIncomeShouldSucceed() {
        Date startDate = new Date(1318386508000L);
        Date endDate = new Date(1318386508009L);
        when(transactionsRepo.getTotalIncome(startDate, endDate)).thenReturn(500F);

        NumericResponse response = managerController.getTotalIncome(startDate, endDate);

        assertTrue(response.isSuccess());
        assertThat(response.getMessage(), isEmptyOrNullString());
        assertThat("Invalid total income", response.getNumber().floatValue() == 500F);
        verify(transactionsRepo, times(1)).getTotalIncome(startDate, endDate);
    }


    @DisplayName("Test getIncomeToday. should return 0")
    @Test
    void getIncomeToday() {

        NumericResponse response = managerController.getIncomeToday();

        assertTrue(response.isSuccess());
        assertThat(response.getMessage(), isEmptyOrNullString());
        assertThat("Invalid total income", response.getNumber().floatValue() == 0F);

    }
}
