package controller;

import com.uploadcare.upload.UploadFailureException;
import main.manager.ManagerApp;
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;
import model.entity.Dish;
import model.repository.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @InjectMocks
    private CustomerController customerController;

    @InjectMocks
    private ManagerController managerController;

    private Dish testDish1, testDish2, testDish3;


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
                .imagePath("photos/meat.jpg")
                .rate(4.5F)
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


}
