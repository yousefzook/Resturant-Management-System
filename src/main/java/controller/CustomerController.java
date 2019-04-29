package controller;

import model.OrderState;
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;
import model.entity.Dish;
import model.entity.Order;
import model.entity.Table;
import model.entity.Transaction;
import model.repository.DishRepository;
import model.repository.OrderRepository;
import model.repository.TableRepository;
import model.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomerController {

    @Autowired
    private DishRepository dishRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private TableRepository tableRepo;

    @Autowired
    private TransactionRepository transRepo;

    @Autowired
    private UploadCareService uploadCareService;

    /**
     * get all available dishes
     *
     * @return dishResponse containing all the dishes or the exception message if error.
     */
    public DishResponse getDishes() {
        DishResponse response = new DishResponse();
        response.setSuccess(false);

        try {
            response.setDishes(dishRepo.findAllByActive(true));
            for (Dish d : response.getDishes())
                d.setImagePath(uploadCareService.downloadImageFromCloud(d.getImagePath()));
            response.setSuccess(true);
        } catch (IOException e) {
            response.setMessage(e.getMessage());
        }
        return response;
    }


    /**
     * when customer confirms the order, store this order into the database and save the transactions
     *
     * @param order the order the customer made
     * @return EmptyResponse to confirm the success or failure of the invocation.
     */
    public EmptyResponse confirmOrder(Order order) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);
        Optional<Table> tableOptional = tableRepo.findById(order.getTable().getId());
        if (!tableOptional.isPresent()) {
            response.setMessage("Table does not exist");
        } else {
            Table table = tableOptional.get();
            if (order.getDetails().isEmpty())
                response.setMessage("Empty order is not allowed!");
            else {
                orderRepo.save(order);
                // for each dish, save it in the transaction
                for (Map.Entry<Dish, Integer> entry : order.getDetails().entrySet()) {
                    Transaction t = Transaction.builder()
                            .dish(entry.getKey())
                            .amountPhy(entry.getValue())
                            .amountFin(entry.getValue() * entry.getKey().getPrice())
                            .date(new Date(System.currentTimeMillis()))
                            .order(order)
                            .table(table)
                            .build();
                    transRepo.save(t);
                }
                response.setSuccess(true);
            }
        }
        return response;
    }

    public EmptyResponse rateOrder(Order order, int rate) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);

        List<Dish> inOrderDishes = dishRepo.findAllById(
                order.getDetails().keySet().stream()
                        .map(Dish::getId).collect(Collectors.toList())
        );

        if (inOrderDishes.size() != order.getDetails().size()) {
            response.setMessage("One of the dishes in the order is not valid");
        } else {
            inOrderDishes.forEach(d -> {
                float newRate = (d.getRate() * d.getRateCount() + rate) / (d.getRateCount() + 1);
                d.setRateCount(d.getRateCount() + 1);
                d.setRate(newRate);
                dishRepo.save(d);
            });
            dishRepo.saveAll(inOrderDishes);
        }
        return response;
    }

    public OrderState getOrderState(int orderId) {
        return orderRepo.findById(orderId).get().getState();
    }

}
