package controller;

import model.OrderState;
import model.actionresults.CookResponse;
import model.actionresults.EmptyResponse;
import model.actionresults.OrderResponse;
import model.entity.Cook;
import model.entity.Order;
import model.repository.CookRepository;
import model.repository.DishRepository;
import model.repository.OrderRepository;
import model.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Component
public class CookController {

    @Autowired
    private DishRepository dishRepo;

    @Autowired
    private CookRepository cookRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private UploadCareService uploadCareService;

    @PersistenceContext
    private EntityManager em;

    //List cook ids
    // update state
    // List of all inQueue orders
    // List of all assigned to this cook

    public CookResponse getCooks() {
        CookResponse response = new CookResponse();
        response.setSuccess(true);
        response.setCooks(cookRepo.getAllHiredWithoutOrders());
        return response;
    }

    public EmptyResponse updateOrderState(int cookId, int orderId, OrderState newState) throws Exception {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);

        Optional<Order> optionalOrder = orderRepo.findById(orderId);
        if (!optionalOrder.isPresent()) {
            response.setMessage(String.format("Order with id %d does not exist", orderId));
            return response;
        }

        OrderState oldState = optionalOrder.get().getState();
        Optional<Cook> optionalCook = cookRepo.findById(cookId);


        if (oldState == newState) {
            response.setMessage("Order state is already set to " + newState);
            return response;
        }

        if (!optionalCook.isPresent()) {
            response.setMessage(String.format("Cook with id %d does not exist", cookId));
            return response;
        }

        switch (oldState) {
            case inQueue:
                if (newState == OrderState.Done) {
                    response.setMessage("Cannot set an inQueue order directly to done");
                } else {
                    optionalOrder.get().setState(newState);
                    optionalOrder.get().setCook(cookRepo.getOne(cookId));
                    orderRepo.save(optionalOrder.get());
                    optionalCook.get().getAssignedOrders().add(optionalOrder.get());
                    cookRepo.save(optionalCook.get());
                    response.setSuccess(true);
                }
                break;
            case Assigned:
                if (newState == OrderState.inQueue) {
                    response.setMessage("Cannot set an assigned order inQueue");
                } else {
                    optionalOrder.get().setState(newState);
                    orderRepo.save(optionalOrder.get());
                    optionalCook.get().getAssignedOrders().remove(optionalOrder.get());
                    cookRepo.save(optionalCook.get());
                    response.setSuccess(true);
                }
                break;
            case Done:
                response.setMessage("Cannot alter done orders");
                break;
            default:
                throw new Exception("Invalid OrderState");
        }
        return response;
    }

    public OrderResponse getInQueueOrders() {
        OrderResponse response = new OrderResponse();
        response.setSuccess(true);
        return response;
    }

    public OrderResponse getOrdersAssignedTo(int cookId) {
        OrderResponse response = new OrderResponse();
        response.setSuccess(false);

        Optional<Cook> optionalCook = cookRepo.findById(cookId);
        if (!optionalCook.isPresent()) {
            response.setMessage(String.format("Cook with id %d does not exist", cookId));
        } else {
            List<Order> assignedOrders = optionalCook.get().getAssignedOrders();
            response.setOrders(assignedOrders);
        }

        return response;
    }
}
