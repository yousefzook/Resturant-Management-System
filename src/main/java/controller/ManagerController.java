package controller;

import com.sun.istack.internal.NotNull;
import model.Cook;
import model.DBMethods;
import model.Dish;
import model.actionresults.CookResponse;
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;
import model.actionresults.NumericResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ManagerController {
    private DBMethods db;

    public EmptyResponse addDish(@NotNull Dish dishToAdd) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);

        if (StringUtils.isBlank(dishToAdd.getName()) ||
                StringUtils.isBlank(dishToAdd.getDescription())) {
            response.setMessage("Dish name and description can not be empty, null nor a whitespace");
        } else if (dishToAdd.getPrice() < 0 ||
                dishToAdd.getRateCount() < 0 ||
                dishToAdd.getTimeToPrepare() < 0) {
            response.setMessage("Dish price, rate, time to prepare cannot be less than zero");
        } else {
            try {
                db.addDish(dishToAdd);
                response.setSuccess(true);
            } catch (Exception e) {
                response.setMessage(e.getMessage());
            }
        }
        return response;
    }

    public DishResponse getDishes() {
        DishResponse response = new DishResponse();
        response.setSuccess(false);

        try {
            List<Dish> allDishes = Arrays.asList(db.getDishes(null));
            response.setSuccess(true);
            response.setDishes(allDishes);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        return response;
    }


    public EmptyResponse updateDish(@NotNull Dish oldDish, @NotNull Dish newDish) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);

        if (oldDish.getId() < 0) {
            response.setMessage("Dish id cannot be less than zero");
        } else if (StringUtils.isBlank(newDish.getName()) ||
                StringUtils.isBlank(newDish.getDescription())) {
            response.setMessage("Dish name and description can not be empty, null nor a whitespace");
        } else if (newDish.getPrice() < 0 ||
                newDish.getRateCount() < 0 ||
                newDish.getTimeToPrepare() < 0) {
            response.setMessage("Dish price, rate, time to prepare cannot be less than zero");
        } else {
            try {
                db.updateDish(oldDish, newDish);
                response.setSuccess(true);
            } catch (Exception e) {
                response.setMessage(e.getMessage());
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
            try {
                db.removeDish(dishId);
                response.setSuccess(true);
            } catch (Exception e) {
                response.setMessage(e.getMessage());
            }
        }
        return response;
    }

    public CookResponse getCooks() {
        CookResponse response = new CookResponse();
        //TODO implement this after adding the functionality to the db interface.
        return response;
    }

    public EmptyResponse addCook(@NotNull Cook cook) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);

        if (StringUtils.isBlank(cook.getFirstName()) || StringUtils.isBlank(cook.getLastName())) {
            response.setMessage("Cook first name and last name cannot be null, empty nor blank");
        } else {
            try {
                db.addCook(cook);
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
            try {
                db.fireCook(null); //TODO fix this to be cookId after changing the db interface.
                response.setSuccess(true);
            } catch (Exception e) {
                response.setMessage(e.getMessage());
            }
        }
        return response;
    }

    public NumericResponse getTotalIncome(@NotNull Date startDate, @NotNull Date endDate) {
        NumericResponse response = new NumericResponse();
        response.setSuccess(false);

        if (startDate.after(endDate)) {
            response.setMessage("End date must be after start date.");
        } else {
            try {
                db.getTotalIncome(startDate, endDate);
                response.setSuccess(true);
            } catch (Exception e) {
                response.setMessage(e.getMessage());
            }
        }
        return response;
    }
}
