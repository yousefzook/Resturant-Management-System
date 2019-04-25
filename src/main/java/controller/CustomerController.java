package controller;

import model.OrderState;
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;
import model.entity.*;
import model.repository.CookRepository;
import model.repository.DishRepository;
import model.repository.OrderRepository;
import model.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.Map;


public class CustomerController {

    @Autowired
    private DishRepository dishRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private CookRepository cookRepo;

    @Autowired
    private TransactionRepository transRepo;

    @Autowired
    private UploadCareService uploadCareService;

    /**
     * get all available dishes
     *
     * @return
     */
    public DishResponse getDishes() {
        DishResponse response = new DishResponse();
        response.setSuccess(false);

        try {
            response.setDishes(dishRepo.findAllByActive(true));
            response.setDishes(dishRepo.findAllByActive(true));
            for (Dish d : response.getDishes())
                d.setImagePath(uploadCareService.downloadImageFromCloud(d.getImagePath()));
            response.setSuccess(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    /**
     * when customer confirms the order, store this order into the database and save the transactions
     *
     * @param order
     * @return
     */
    public EmptyResponse confirmOrder(Order order, Table table) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);
        Cook cook = cookRepo.findById((int) order.getCook().getId()).get();
        if (order.getDetails().isEmpty())
            response.setMessage("Empty order is not allowed!");
        else if (cook == null || !cook.getHired())
            response.setMessage("Cook is not exist or he's no longer active!");
        else {
            // for each dish, save it in the transaction
            for (Map.Entry<Dish, Integer> entry : order.getDetails().entrySet()) {
                Transaction t = Transaction.builder()
                        .dish(entry.getKey())
                        .amountPhy(entry.getValue())
                        .amountFin(entry.getValue() * entry.getKey().getPrice())
                        .order(order)
                        .date(new Date())
                        .table(table)
                        .build();
                transRepo.save(t);
            }
            orderRepo.save(order);
            response.setSuccess(true);
        }

        return response;
    }

    public OrderState getOrderState(int orderId) {
        OrderState state = OrderState.inQueue;

        return state;
    }


}
