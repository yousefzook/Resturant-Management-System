package controller;

import lombok.Setter;
import model.Cook;
import model.DBMethods;
import model.Dish;
import model.actionresults.CookResponse;
import model.actionresults.DishResponse;
import model.actionresults.EmptyResponse;
import model.actionresults.NumericResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class ManagerController {
    private static ManagerController instance;

    @Setter
    private DBMethods db;

    private ManagerController() {
    }

    public static ManagerController getInstance() {
        if (instance == null)
            instance = new ManagerController();
        return instance;
    }

    public EmptyResponse addDish(Dish dishToAdd) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);

        if (StringUtils.isBlank(dishToAdd.getName()) ||
                StringUtils.isBlank(dishToAdd.getDescription()) ||
                StringUtils.isBlank(dishToAdd.getImagePath())) {
            response.setMessage("Dish name, description and imagePath can't be empty, null nor a whitespace");
        } else if (dishToAdd.getPrice() < 0 ||
                dishToAdd.getTimeToPrepare() < 0) {
            response.setMessage("Dish price, rate, time to prepare cannot be less than zero");
        }

//        if (checkImage(dishToAdd, response)) {
        try {
            db.addDish(dishToAdd);
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
//            }
        }

        return response;
    }

//    private boolean checkImage(Dish dishToAdd, EmptyResponse response) {
//        File tempFile = new File(dishToAdd.getImagePath());
//        System.out.println(dishToAdd.getImagePath());
//        if (tempFile.exists()) {
//            File f = new File(dishToAdd.getImagePath());
//            try {
//                if (ImageIO.read(f.getAbsoluteFile()) != null) {
//                    dishToAdd.setImage(Files.readAllBytes(f.toPath()));
//                    return true;
//                } else {
//                    response.setMessage("The file is not an image");
//                    return false;
//                }
//            } catch (IOException e) {
//                response.setMessage(e.getMessage());
//                return false;
//            }
//        } else {
//            response.setMessage("Invalid path");
//            return false;
//        }
//    }

    public DishResponse getDishes() {
        DishResponse response = new DishResponse();
        response.setSuccess(false);

        try {
            response.setDishes(db.getAvailableDishes());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        return response;
    }


    public EmptyResponse updateDish(int oldDishId, Dish newDish) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);

        if (oldDishId < 0) {
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
                db.updateDish(oldDishId, newDish);
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
        response.setSuccess(false);
        try {
            response.setCooks(db.getCooks());
            response.setSuccess(true);
        } catch (Exception e) {
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public EmptyResponse addCook(Cook cook) {
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
                db.fireCook(cookId);
                response.setSuccess(true);
            } catch (Exception e) {
                response.setMessage(e.getMessage());
            }
        }
        return response;
    }

    public NumericResponse getIncomeToday() {
        Date startDate = Date.from(LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault()).toInstant());

        Date endDate = DateUtils.addMilliseconds(
                DateUtils.ceiling(
                        new Date(System.currentTimeMillis()), Calendar.DATE
                ), -1);
        return getTotalIncome(startDate, endDate);
    }

    public NumericResponse getIncomeThisMonth() {
        Date startDate = Date.from(LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());

        Date endDate = new Date(System.currentTimeMillis());
        return getTotalIncome(startDate, endDate);
    }


    private NumericResponse getTotalIncome(Date startDate, Date endDate) {
        NumericResponse response = new NumericResponse();
        response.setSuccess(false);

        if (startDate.after(endDate)) {
            response.setMessage("End date must be after start date.");
        } else {
            try {
                response.setNumber(db.getTotalIncome(startDate, endDate));
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
        } else {
            try {
                response.setDishes(db.getTopDishes(limit));
                response.setSuccess(true);
            } catch (Exception e) {
                response.setMessage(e.getMessage());
            }
        }

        return response;
    }

    public CookResponse getTopCooks(int limit) {
        CookResponse response = new CookResponse();
        response.setSuccess(false);

        if (limit < 1) {
            response.setMessage("Limit must be a positive integer");
        } else {
            try {
                response.setCooks(db.getTopCooks(limit));
                response.setSuccess(true);
            } catch (Exception e) {
                response.setMessage(e.getMessage());
            }
        }

        return response;
    }
}
