package controller;

import main.customer.CustomerApp;
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;
import model.entity.Dish;
import model.entity.Order;
import model.entity.Table;
import model.entity.Transaction;
import model.repository.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CustomerApp.class)
public class CustomerControllerTest {
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
    private TransactionRepository transactionRepo;

    @InjectMocks
    private CustomerController customerController;

    private Dish testDish1, testDish2, testDish3;
    private String testDish1ImagePath = "photos/Tandoori chicken.jpg";
    private String testDish2ImagePath = "photos/meat.jpg";
    private String testDish3ImagePath = "photos/Chicken-Tikka.jpg";
    private List<Dish> dishesList;
    private Order testOrder;
    private List<Table> testTables;

    /*
     * UUID1 --> photos/Tandoori chicken.jpg
     * UUID2 --> photos/meat.jpg
     * UUID3 --> photos/Chicken-Tikka.jpg
     */

    @BeforeEach
    private void setUpEach() {
        testDish1 = Dish.builder()
                .id(1)
                .name("Test Dish 1")
                .description("Description 1")
                .active(true)
                .price(15F)
                .timeToPrepare(15)
                .imagePath("UUID1")
                .rate(5F)
                .rateCount(0)
                .build();

        testDish2 = Dish.builder()
                .id(2)
                .name("Test Dish 2")
                .description("Description 2")
                .active(true)
                .price(10F)
                .timeToPrepare(10)
                .imagePath("UUID2")
                .rate(4.5F)
                .rateCount(1)
                .build();

        testDish3 = Dish.builder()
                .id(3)
                .name("Test Dish 3")
                .description("Description 3")
                .active(false)
                .price(20F)
                .timeToPrepare(15)
                .imagePath("UUID3")
                .rate(3.5F)
                .rateCount(1)
                .build();


        testTables = IntStream.range(0, 5).mapToObj(Table::new).collect(Collectors.toList());
        dishesList = Arrays.asList(testDish1, testDish2, testDish3);
        Map<Dish, Integer> testOrderDetails = new HashMap<>();
        testOrderDetails.put(testDish1, 2);
        testOrderDetails.put(testDish2, 3);
        testOrder = new Order(testOrderDetails);
        testOrder.setTable(testTables.get(new Random().nextInt(testTables.size())));
    }

    @DisplayName("Test getDishes which should return all active dishes")
    @Test
    void TestGetDishes() throws IOException {
        Dish resultDish1 = testDish1.toBuilder().imagePath(testDish1ImagePath).build();
        Dish resultDish2 = testDish2.toBuilder().imagePath(testDish2ImagePath).build();

        String dish1UUID = testDish1.getImagePath();
        String dish2UUID = testDish2.getImagePath();

        when(dishRepo.findAllByActive(true)).thenReturn(dishesList.stream().filter(Dish::isActive).collect(Collectors.toList()));
        when(uploadCareService.downloadImageFromCloud(eq(testDish1.getImagePath()))).thenReturn(testDish1ImagePath);
        when(uploadCareService.downloadImageFromCloud(eq(testDish2.getImagePath()))).thenReturn(testDish2ImagePath);

        DishResponse response = customerController.getDishes();

        assertTrue(response.isSuccess());
        assertThat(response.getDishes(), Matchers.contains(resultDish1, resultDish2));
        assertThat(response.getDishes().size(), is(2));

        verify(dishRepo, times(1)).findAllByActive(true);
        ArgumentCaptor<String> urlsCaptor = ArgumentCaptor.forClass(String.class);
        verify(uploadCareService, times(2)).downloadImageFromCloud(urlsCaptor.capture());
        assertThat(urlsCaptor.getAllValues(), Matchers.contains(dish1UUID, dish2UUID));
    }

    @DisplayName("Test confirm order with invalid table id")
    @Test
    void TestConfirmOrderWithInvalidTable() {
        when(tableRepo.findById(eq(testOrder.getTable().getId()))).thenReturn(Optional.empty());
        EmptyResponse response = customerController.confirmOrder(testOrder);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
        verify(tableRepo, times(1)).findById(testOrder.getTable().getId());
    }

    @DisplayName("Test confirm order with empty details")
    @Test
    void TestConfirmOrderWithEmptyDetails() {
        testOrder.setDetails(new HashMap<>());
        when(tableRepo.findById(eq(testOrder.getTable().getId())))
                .thenReturn(Optional.of(testTables.get(testOrder.getTable().getId())));

        EmptyResponse response = customerController.confirmOrder(testOrder);

        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
        verify(tableRepo, times(1)).findById(testOrder.getTable().getId());
    }

    @DisplayName("Test confirm which should save a transaction for each dish in the order.")
    @Test
    void TestConfirmOrderNormalCase() {
        when(tableRepo.findById(eq(testOrder.getTable().getId())))
                .thenReturn(Optional.of(testTables.get(testOrder.getTable().getId())));
        when(orderRepo.save(testOrder)).thenReturn(testOrder);
        when(dishRepo.findAllByIdInAndActiveTrue(
                testOrder.getDetails().keySet()
                        .stream()
                        .map(Dish::getId)
                        .collect(Collectors.toList())
                )
        ).thenReturn(new ArrayList<>(testOrder.getDetails().keySet()));

        when(transactionRepo.save(ArgumentMatchers.any(Transaction.class))).thenReturn(null);

        EmptyResponse response = customerController.confirmOrder(testOrder);

        assertTrue(response.isSuccess());
        assertThat(response.getMessage(), isEmptyOrNullString());
        verify(tableRepo, times(1)).findById(testOrder.getTable().getId());
        verify(orderRepo, times(1)).save(testOrder);
        verify(dishRepo, times(1)).findAllByIdInAndActiveTrue(anyList());
        ArgumentCaptor<Transaction> transactionsCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepo, times(2)).save(transactionsCaptor.capture());
        List<Transaction> transactions = transactionsCaptor.getAllValues();
        transactions.sort(Comparator.comparingInt(Transaction::getAmountPhy));
        assertThat(transactions.size(), is(2));
        assertThat(transactions.get(0).getAmountFin(), is(testDish1.getPrice() * 2));
        assertThat(transactions.get(1).getAmountFin(), is(testDish2.getPrice() * 3));
    }
}
