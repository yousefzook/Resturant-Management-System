package controller;

import model.actionresults.CookResponse;
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;
import model.actionresults.NumericResponse;
import model.entity.Cook;
import model.entity.Dish;
import model.repository.CookRepository;
import model.repository.DishRepository;
import model.repository.OrderRepository;
import model.repository.TransactionsRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Component
public class ManagerController {

    @Autowired
    private DishRepository dishRepo;

    @Autowired
    private CookRepository cookRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private TransactionsRepository transactionsRepo;

    public EmptyResponse addDish(Dish dishToAdd) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);

        if (StringUtils.isBlank(dishToAdd.getName()) ||
                StringUtils.isBlank(dishToAdd.getDescription()) ||
                StringUtils.isBlank(dishToAdd.getImagePath())) {
            response.setMessage("Dish name, description and imagePath can't be empty, null nor a whitespace");
        } else if (dishToAdd.getPrice() == null || dishToAdd.getPrice() < 0 ||
                dishToAdd.getTimeToPrepare() == null || dishToAdd.getTimeToPrepare() < 0) {
            response.setMessage("Dish price and time to prepare cannot be less than zero");
        } else {
            dishToAdd.setImagePath(saveImageToCloud(dishToAdd.getImagePath()));
            dishRepo.save(dishToAdd);
            response.setSuccess(true);
        }

        return response;
    }

    public DishResponse getDishes() {
        DishResponse response = new DishResponse();
        response.setDishes(dishRepo.findAll());
        response.setSuccess(true);
        return response;
    }


    public EmptyResponse updateDish(int oldDishId, Dish newDish) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);

        if (oldDishId < 0) {
            response.setMessage("Dish id cannot be less than zero");
        } else if (newDish.getPrice() != null && newDish.getPrice() < 0 ||
                newDish.getTimeToPrepare() != null && newDish.getTimeToPrepare() < 0) {
            response.setMessage("Dish price and time to prepare cannot be less than zero");
        } else {
            Optional<Dish> oldDishOptional = dishRepo.findById(oldDishId);
            if (oldDishOptional.isPresent()) {
                Dish oldDish = oldDishOptional.get();
                if (newDish.getTimeToPrepare() != null) oldDish.setTimeToPrepare(newDish.getTimeToPrepare());
                if (newDish.getPrice() != null) oldDish.setPrice(newDish.getPrice());
                if (newDish.getName() != null) oldDish.setName(newDish.getName());
                if (newDish.getDescription() != null) oldDish.setDescription(newDish.getDescription());
                if (newDish.getImagePath() != null) {
                    String imageUrl = saveImageToCloud(newDish.getImagePath());
                    oldDish.setImagePath(imageUrl);
                }
                dishRepo.save(oldDish);
                response.setSuccess(true);
            }
        }
        return response;
    }

    public EmptyResponse removeDish(int dishId) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);

        if (dishId < 0) {
            response.setMessage("Dish id cannot be less than zero.");
        } else {
            Optional<Dish> dishToRemove = dishRepo.findById(dishId);
            if (!dishToRemove.isPresent()) {
                response.setMessage("No such a dish with the given id");
            } else if (dishToRemove.get().isActive()) {
                response.setMessage("Dish is already not active");
            } else {
                dishToRemove.get().setActive(true);
                dishRepo.save(dishToRemove.get());
                response.setSuccess(true);
            }
        }
        return response;
    }

    public CookResponse getCooks() {
        CookResponse response = new CookResponse();
        response.setSuccess(false);
        response.setCooks(cookRepo.getAllWithoutOrders());
        response.setSuccess(true);
        return response;
    }

    public EmptyResponse addCook(Cook cook) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);

        if (StringUtils.isBlank(cook.getFirstName()) || StringUtils.isBlank(cook.getLastName())) {
            response.setMessage("Cook first name and last name cannot be null, empty nor blank");
        } else if (cook.getId() != null) {
            response.setMessage("Cook id cannot be preset");
        } else {
            try {
                cookRepo.save(cook);
                response.setSuccess(true);
            } catch (Exception e) {
                response.setMessage(e.getMessage());
            }
        }
        return response;
    }

    public EmptyResponse fireCook(int cookId) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);

        if (cookId < 0) {
            response.setMessage("Cook id cannot be less than zero.");
        } else {
            Optional<Cook> cookOptional = cookRepo.findById(cookId);
            if (!cookOptional.isPresent()) {
                response.setMessage("Cannot find a cook with id = " + cookId);
            } else if (!cookOptional.get().isHired()) {
                response.setMessage("Cook with id = " + cookId + " was already fired");
            } else {
                cookOptional.get().setHired(false);
                response.setSuccess(true);
            }
        }
        return response;
    }

    public NumericResponse getIncomeToday() {
        Date startDate = Date.from(LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault()).toInstant());

        Date endDate = DateUtils.addMilliseconds(
                DateUtils.ceiling(new Date(System.currentTimeMillis()), Calendar.DATE), -1);
        return getTotalIncome(startDate, endDate);
    }

    public NumericResponse getIncomeThisMonth() {
        Date startDate = Date.from(LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant());

        Date endDate = new Date(System.currentTimeMillis());
        return getTotalIncome(startDate, endDate);
    }


    public NumericResponse getTotalIncome(Date startDate, Date endDate) {
        NumericResponse response = new NumericResponse();
        response.setSuccess(false);

        if (startDate.after(endDate)) {
            response.setMessage("End date must be after start date.");
        } else {
            try {

                response.setNumber(transactionsRepo.getTotalIncome(startDate, endDate));
                response.setSuccess(true);
            } catch (Exception e) {
                response.setMessage(e.getMessage());
            }
        }
        return response;
    }

    public DishResponse getTopDishes(int limit) {
        DishResponse response = new DishResponse();
        response.setSuccess(false);

        if (limit < 1) {
            response.setMessage("Limit must be a positive integer");
        } else if (dishRepo.count() == 0) {
            response.setMessage("No dishes yet");
        } else {
            response.setDishes(dishRepo.getTopDishes(limit));
            response.setSuccess(true);
        }

        return response;
    }

    public CookResponse getTopCooks(int limit) {
        CookResponse response = new CookResponse();
        response.setSuccess(false);

        if (limit < 1) {
            response.setMessage("Limit must be a positive integer");
        } else if (cookRepo.count() == 0) {
            response.setMessage("No cooks are hired yet");
        } else {
            response.setCooks(cookRepo.getTopCooks(limit));
            response.setSuccess(true);
        }

        return response;
    }

    private String saveImageToCloud(String imagePath) {
        //TODO implement this
        return null;
    }
}
