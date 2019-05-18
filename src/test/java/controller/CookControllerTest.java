package controller;

import main.cook.CookApp;
import model.OrderState;
import model.actionresults.CookResponse;
import model.actionresults.EmptyResponse;
import model.actionresults.OrderResponse;
import model.entity.Cook;
import model.entity.Dish;
import model.entity.Order;
import model.entity.Table;
import model.repository.CookRepository;
import model.repository.OrderRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CookApp.class)
public class CookControllerTest {


    @Mock
    private CookRepository cookRepo;

    @Mock
    private OrderRepository orderRepo;

    @InjectMocks
    private CookController cookController;

    private Cook cook1;
    private Cook cook2;
    private Cook cook3;
    private Order testOrder;
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
        cook3 = Cook.builder()
                .id(3)
                .firstName("cook3")
                .lastName("name3")
                .hired(false)
                .build();
    }

    @DisplayName("Test getCooks")
    @Test
    void TestGetCooks() {
        when(cookRepo.getAllHiredWithoutOrders()).thenReturn(Collections.singletonList(cook1));
        CookResponse response = cookController.getCooks();

        assertTrue(response.isSuccess());
        verify(cookRepo, times(1)).getAllHiredWithoutOrders();
        assertThat(response.getCooks(), Matchers.hasSize(1));
        assertThat(response.getCooks().get(0), Matchers.equalTo(cook1));
    }


    @DisplayName("Test updateOrderState, put inqueue dish to done state directly, should return error message")
    @Test
    void TestUpdateDishStateInQueueToDone() {
        when(cookRepo.getAllHiredWithoutOrders()).thenReturn(Collections.singletonList(cook1));
        when(cookRepo.findById(1)).thenReturn(Optional.of(cook1));
        when(orderRepo.findById(0)).thenReturn(Optional.of(testOrder));
        EmptyResponse response = null;
        try {
            response = cookController.updateOrderState(1, 0, OrderState.Done);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test updateOrderState, put done dish to inQueue state, should return error message")
    @Test
    void TestUpdateDishStateDoneToInQueue() {
        when(cookRepo.getAllHiredWithoutOrders()).thenReturn(Collections.singletonList(cook1));
        when(cookRepo.findById(1)).thenReturn(Optional.of(cook1));
        when(orderRepo.findById(0)).thenReturn(Optional.of(testOrder));
        testOrder.setState(OrderState.Done);
        EmptyResponse response = null;
        try {
            response = cookController.updateOrderState(1, 0, OrderState.inQueue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test updateOrderState, put Assigned dish to inQueue state, should return error message")
    @Test
    void TestUpdateDishStateAssignedToInQueue() {
        when(cookRepo.getAllHiredWithoutOrders()).thenReturn(Collections.singletonList(cook1));
        when(cookRepo.findById(1)).thenReturn(Optional.of(cook1));
        when(orderRepo.findById(0)).thenReturn(Optional.of(testOrder));
        testOrder.setState(OrderState.Assigned);
        EmptyResponse response = null;
        try {
            response = cookController.updateOrderState(1, 0, OrderState.inQueue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }

    @DisplayName("Test updateOrderState, put Assigned dish to done state, should return success")
    @Test
    void TestUpdateDishStateAssignedToDone() {
        cook2.getAssignedOrders().remove(testOrder);
        when(cookRepo.getAllHiredWithoutOrders()).thenReturn(Collections.singletonList(cook1));
        when(cookRepo.findById(2)).thenReturn(Optional.of(cook2));
        when(orderRepo.findById(0)).thenReturn(Optional.of(testOrder));
        testOrder.setState(OrderState.Assigned);
        EmptyResponse response = null;
        try {
            response = cookController.updateOrderState(2, 0, OrderState.Done);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(response.isSuccess());
        assertThat(response.getMessage(), isEmptyOrNullString());
    }

    @DisplayName("Test updateOrderState, put inQueue dish to Assigned state, should return success")
    @Test
    void TestUpdateDishStateInQueueToAssigned() {
        when(cookRepo.getAllHiredWithoutOrders()).thenReturn(Collections.singletonList(cook1));
        when(cookRepo.findById(1)).thenReturn(Optional.of(cook1));
        when(orderRepo.findById(0)).thenReturn(Optional.of(testOrder));
        testOrder.setState(OrderState.inQueue);
        EmptyResponse response = null;
        try {
            response = cookController.updateOrderState(1, 0, OrderState.Assigned);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(response.isSuccess());
        assertThat(response.getMessage(), isEmptyOrNullString());
    }


    @DisplayName("Test getInQueueOrders, should return testOrder")
    @Test
    void TestGetInQueueOrders() {
        when(cookRepo.getAllHiredWithoutOrders()).thenReturn(Collections.singletonList(cook1));
        when(orderRepo.findOrderByState(OrderState.inQueue)).thenReturn(Collections.singletonList(testOrder));
        testOrder.setState(OrderState.inQueue);
        OrderResponse response = cookController.getInQueueOrders();
        assertTrue(response.isSuccess());
        assertThat(response.getMessage(), isEmptyOrNullString());
    }


    @DisplayName("Test getOrdersAssignedTo invalid cook id, should return error message")
    @Test
    void TestGOrdersAssignedToInvalidCookId() {
        OrderResponse response = cookController.getOrdersAssignedTo(2);
        assertFalse(response.isSuccess());
        assertThat(response.getMessage(), not(isEmptyOrNullString()));
    }


    @DisplayName("Test getOrdersAssignedTo valid cook id, should return success")
    @Test
    void TestGOrdersAssignedToValidCookId() {
        when(cookRepo.findById(1)).thenReturn(Optional.of(cook1));
        OrderResponse response = cookController.getOrdersAssignedTo(1);
        assertTrue(response.isSuccess());
        assertThat(response.getMessage(), isEmptyOrNullString());
    }
}
