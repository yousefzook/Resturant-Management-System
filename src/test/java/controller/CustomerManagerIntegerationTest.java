package controller;

import com.uploadcare.upload.UploadFailureException;
import main.manager.ManagerApp;
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;
import model.actionresults.NumericResponse;
import model.entity.Dish;
import model.entity.Order;
import model.entity.Table;
import model.repository.*;
import org.apache.commons.lang3.time.DateUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = ManagerApp.class)
public class CustomerManagerIntegerationTest {

    @Mock
    private DishRepository dishRepo;

    @Mock
    private UploadCareService uploadCareService;

    @Mock
    private TransactionRepository transactionRepo;

    @InjectMocks
    private CustomerController customerController;

    @InjectMocks
    private ManagerController managerController;

    private Dish testDish1, testDish2, testDish3;

    private Order testOrder;

    private List<Table> testTables;


    @BeforeEach
    private void setUpEach() {
        testDish1 = Dish.builder()
                .id(1)
                .name("Test Dish 1")
                .description("Description 1")
                .active(true)
                .price(15F)
                .timeToPrepare(15)
                .imagePath("photos/Tandoori chicken.jpg")
                .rate(1F)
                .rateCount(1)
                .build();

        testDish2 = Dish.builder()
                .id(2)
                .name("Test Dish 2")
                .description("Description 2")
                .active(true)
                .price(10F)
                .timeToPrepare(10)
                .imagePath("photos/meat.jpg")
                .rate(1.5F)
                .rateCount(1)
                .build();

        testDish3 = Dish.builder()
                .id(3)
                .name("Test Dish 3")
                .description("Description 3")
                .active(true)
                .price(20F)
                .timeToPrepare(15)
                .imagePath("photos/Chicken-Tikka.jpg")
                .rate(3.5F)
                .rateCount(1)
                .build();

        testTables = IntStream.range(0, 5).mapToObj(Table::new).collect(Collectors.toList());
        Map<Dish, Integer> testOrderDetails = new HashMap<>();
        testOrderDetails.put(testDish1, 2);
        testOrder = new Order(testOrderDetails);
        testOrder.setTable(testTables.get(new Random().nextInt(testTables.size())));

    }


    @DisplayName("Test getDishes after manager addDish is called which should return the new dish added in the dishes list")
    @Test
    void TestGetDishesAfterAddDish() throws IOException, UploadFailureException {

        /*
         * get dishes should return dish1 and dish 2
         * */
        List<Dish> dishesList = new ArrayList<Dish>();
        dishesList.add(testDish1);
        dishesList.add(testDish2);

        when(dishRepo.findAllByActive(true)).thenReturn(dishesList.stream().filter(Dish::isActive).collect(Collectors.toList()));
        when(uploadCareService.downloadImageFromCloud(eq(testDish1.getImagePath()))).thenReturn("photos/Tandoori chicken.jpg");
        when(uploadCareService.downloadImageFromCloud(eq(testDish2.getImagePath()))).thenReturn("photos/meat.jpg");

        DishResponse response = customerController.getDishes();

        assertTrue(response.isSuccess());
        assertThat(response.getDishes(), Matchers.contains(testDish1, testDish2));
        assertThat(response.getDishes().size(), is(2));


        /*
         * add Dish3
         */
        String localImagePath = testDish3.getImagePath();
        when(uploadCareService.saveImageToCloud(testDish3.getImagePath())).thenReturn("UUID");
        when(dishRepo.save(testDish3)).thenReturn(testDish3);
        EmptyResponse addResp = managerController.addDish(testDish3);
        assertTrue(addResp.isSuccess());
        verify(dishRepo, times(1)).save(testDish3);
        verify(uploadCareService, times(1)).saveImageToCloud(localImagePath);

        dishesList.add(testDish3);
        when(dishRepo.findAllByActive(true)).thenReturn(dishesList.stream().filter(Dish::isActive).collect(Collectors.toList()));
        when(uploadCareService.downloadImageFromCloud(eq(testDish3.getImagePath()))).thenReturn("photos/Chicken-Tikka.jpg");


        /*
         * get dishes should return dish1, dish2 and dish3
         * */
        response = customerController.getDishes();

        assertTrue(response.isSuccess());
        assertThat(response.getDishes(), Matchers.contains(testDish1, testDish2, testDish3));
        assertThat(response.getDishes().size(), is(3));

    }


    @DisplayName("Test rating order then manager gets top dishes")
    @Test
    void TestRateThenGetTop() throws IOException, UploadFailureException {
        List<Dish> dishesList = new ArrayList<Dish>();
        dishesList.add(testDish1);

        when(dishRepo.findAllById(testOrder.getDetails().keySet().stream()
                .map(Dish::getId).collect(Collectors.toList()))).thenReturn(dishesList.stream().filter(Dish::isActive).collect(Collectors.toList()));


        EmptyResponse response = customerController.rateOrder(testOrder, 5);
        assertThat(response.isSuccess(), is(true));

        Dish tempDish = testDish1.toBuilder().rate(1F).rateCount(1).build();
        tempDish.setRateCount(tempDish.getRateCount() + 1);
        tempDish.setRate((tempDish.getRate() + 5) / tempDish.getRateCount());
        when(dishRepo.getTopDishes(1)).thenReturn(Arrays.asList(tempDish).stream().filter(Dish::isActive).collect(Collectors.toList()));
        when(dishRepo.count()).thenReturn(1L);

        DishResponse topDishesResponse = managerController.getTopDishes(1);
        assertThat(topDishesResponse.getDishes().size(), is(1));
        assertThat(topDishesResponse.getDishes().get(0).getRate(), is(3F));


    }

    @DisplayName("Test update dish then get dishes")
    @Test
    void TestUpdateThenGetDishes() throws IOException, UploadFailureException {

        /*
         * get dishes should return dish1 and dish 2
         * */
        List<Dish> dishesList = new ArrayList<Dish>();
        dishesList.add(testDish1);
        dishesList.add(testDish2);

        when(dishRepo.findAllByActive(true)).thenReturn(dishesList.stream().filter(Dish::isActive).collect(Collectors.toList()));
        when(uploadCareService.downloadImageFromCloud(eq(testDish1.getImagePath()))).thenReturn("photos/Tandoori chicken.jpg");
        when(uploadCareService.downloadImageFromCloud(eq(testDish2.getImagePath()))).thenReturn("photos/meat.jpg");

        DishResponse response = customerController.getDishes();

        assertTrue(response.isSuccess());
        assertThat(response.getDishes(), Matchers.contains(testDish1, testDish2));
        assertThat(response.getDishes().size(), is(2));

        /*
         * update dish2
         */
        when(dishRepo.findById(testDish2.getId())).thenReturn(Optional.of(testDish2));

        Dish tempDish = testDish2.toBuilder().description("Description has been modified").id(5).build();
        EmptyResponse updateResp = managerController.updateDish(2, tempDish);
        assertTrue(updateResp.isSuccess());
        testDish2.setActive(false);
        dishesList.add(tempDish);
        when(dishRepo.findAllByActive(true)).thenReturn(dishesList.stream().filter(Dish::isActive).collect(Collectors.toList()));



        /*
         * get dishes should return dish1 and modified dish2
         * */
        response = customerController.getDishes();
        assertTrue(response.isSuccess());
        assertThat(response.getDishes().size(), is(2));
        assertThat(response.getDishes().get(1).getDescription(), is("Description has been modified"));


    }


    @DisplayName("Test getting total income after adding an order")
    @Test
    void TestOrderThenGetIncome() throws IOException, UploadFailureException {
        List<Dish> dishesList = new ArrayList<Dish>();
        dishesList.add(testDish1);

        when(dishRepo.findAllById(testOrder.getDetails().keySet().stream()
                .map(Dish::getId).collect(Collectors.toList()))).thenReturn(dishesList.stream().filter(Dish::isActive).collect(Collectors.toList()));


        EmptyResponse response = customerController.rateOrder(testOrder, 5);
        assertThat(response.isSuccess(), is(true));

        Dish tempDish = testDish1.toBuilder().rate(1F).rateCount(1).build();
        tempDish.setRateCount(tempDish.getRateCount() + 1);
        tempDish.setRate((tempDish.getRate() + 5) / tempDish.getRateCount());
        when(dishRepo.getTopDishes(1)).thenReturn(Arrays.asList(tempDish).stream().filter(Dish::isActive).collect(Collectors.toList()));
        when(dishRepo.count()).thenReturn(1L);

        Date startDate = Date.from(LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault()).toInstant());

        Date endDate = DateUtils.addMilliseconds(
                DateUtils.ceiling(new Date(System.currentTimeMillis()), Calendar.DATE), -1);

        when(transactionRepo.getTotalIncome(startDate, endDate)).thenReturn(15F);

        NumericResponse incomeResp = managerController.getIncomeToday();
        assertTrue(incomeResp.isSuccess());
        assertThat(incomeResp.getNumber(), is(testDish1.getPrice()));


    }
}
