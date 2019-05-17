package controller;

import main.cook.CookApp;
import model.OrderState;
import model.actionresults.EmptyResponse;
import model.actionresults.OrderResponse;
import model.entity.*;
import model.repository.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CookApp.class)
public class CookCustomerIntegerationTest {


    @Mock
    private CookRepository cookRepo;

    @Mock
    private TransactionRepository transactionRepo;

    @Mock
    private DishRepository dishRepo;

    @Mock
    private OrderRepository orderRepo;

    @Mock
    private TableRepository tableRepo;

    @InjectMocks
    private CookController cookController;

    @InjectMocks
    private CustomerController customerController;

    private Cook cook1;
    private Cook cook2;
    private Cook cook3;
    private Order testOrder;
    private List<Table> testTables;
    private Dish testDish1, testDish2;

    @BeforeEach
    void setupEach() {
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

        Map<Dish, Integer> testOrderDetails = new HashMap<>();
        testOrderDetails.put(testDish1, 2);
        testOrderDetails.put(testDish2, 3);
        testOrder = new Order(testOrderDetails);
        testOrder.setTable(new Table());
        testOrder.setId(1);
        cook1 = Cook.builder()
                .id(1)
                .firstName("cook1")
                .lastName("name1")
                .hired(true)
                .assignedOrders(new ArrayList<>())
                .build();
        cook2 = Cook.builder()
                .id(2)
                .firstName("cook2")
                .lastName("name2")
                .hired(true)
                .assignedOrders(new ArrayList<>())
                .build();
        cook2.acceptOrder(testOrder);
        cook3 = Cook.builder()
                .id(3)
                .firstName("cook3")
                .lastName("name3")
                .hired(false)
                .build();

        testTables = IntStream.range(0, 5).mapToObj(Table::new).collect(Collectors.toList());
    }


    @DisplayName("Test that order is assigned to cook1")
    @Test
    void TestOrderIsAssignedToCook() {
        when(cookRepo.findById(1)).thenReturn(Optional.of(cook1));
        testOrder.setState(OrderState.inQueue);
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
        when(orderRepo.findById(1)).thenReturn(Optional.of(testOrder));
        when(transactionRepo.save(ArgumentMatchers.any(Transaction.class))).thenReturn(null);


        EmptyResponse response = customerController.confirmOrder(testOrder);

        assertTrue(response.isSuccess());

        try {
            response = cookController.updateOrderState(1, 1, OrderState.Assigned);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(response.getMessage() + " <------------");
        assertTrue(response.isSuccess());

        assertThat(cook1.getAssignedOrders().get(0), Matchers.equalTo(testOrder));

    }

}
