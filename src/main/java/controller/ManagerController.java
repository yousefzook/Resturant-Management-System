package controller;

import com.uploadcare.upload.UploadFailureException;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @Autowired
    private UploadCareService uploadCareService;

    @PersistenceContext
    private EntityManager em;

    public EmptyResponse addDish(Dish dishToAdd) {
        EmptyResponse response = new EmptyResponse();
        response.setSuccess(false);

        if (StringUtils.isBlank(dishToAdd.getName()) ||
                StringUtils.isBlank(dishToAdd.getDescription()) ||
                StringUtils.isBlank(dishToAdd.getImagePath())) {
            response.setMessage("Dish name, description and imagePath can't be empty, null nor a whitespace");
        } else if (dishToAdd.getPrice() == null || dishToAdd.getPrice() < 0 ||
                dishToAdd.getTimeToPrepare() == null || dishToAdd.getTimeToPrepare() < 0) {
            response.setMessage("Dish price and time to prepare cannot be null or less than zero");
        } else {
            try {
                System.out.println(dishToAdd);
                dishToAdd.setImagePath(uploadCareService.saveImageToCloud(dishToAdd.getImagePath()));
                dishRepo.save(dishToAdd);
                response.setSuccess(true);
            } catch (UploadFailureException e) {
                response.setMessage(e.getMessage());
            }
        }
        return response;
    }

    public DishResponse getDishes() {
        DishResponse response = new DishResponse();
        response.setSuccess(false);
        try {
            response.setDishes(dishRepo.findAllByActive(true));
            for (Dish d : response.getDishes()) {
                d.setImagePath(uploadCareService.downloadImageFromCloud(d.getImagePath()));
            }
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

        } else if (newDish.getPrice() != null && newDish.getPrice() < 0 ||
                newDish.getTimeToPrepare() != null && newDish.getTimeToPrepare() < 0) {
            response.setMessage("Dish price and time to prepare cannot be less than zero");
        } else {
            Optional<Dish> oldDishOptional = dishRepo.findById(oldDishId);
            if (oldDishOptional.isPresent()) {
                Dish oldDish = oldDishOptional.get();
                if (newDish.getTimeToPrepare() == null) newDish.setTimeToPrepare(oldDish.getTimeToPrepare());
                if (newDish.getPrice() == null) newDish.setPrice(oldDish.getPrice());
                if (newDish.getName() == null) newDish.setName(oldDish.getName());
                if (newDish.getDescription() == null) newDish.setDescription(oldDish.getDescription());
                if (newDish.getImagePath() == null) {
                    newDish.setImagePath(oldDish.getImagePath());
                } else {
                    try {
                        newDish.setImagePath(uploadCareService.saveImageToCloud(newDish.getImagePath()));
                    } catch (UploadFailureException e) {
                        response.setMessage(e.getMessage());
                    }
                }

                newDish.setRate(oldDish.getRate());
                newDish.setRateCount(oldDish.getRateCount());

                oldDish.setActive(false);
                dishRepo.save(oldDish);
                newDish.setActive(true);
                dishRepo.save(newDish);
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
            } else if (!dishToRemove.get().isActive()) {
                response.setMessage("Dish is already not active");
            } else {
                dishToRemove.get().setActive(false);
                dishRepo.save(dishToRemove.get());
                response.setSuccess(true);
            }
        }
        return response;
    }

    public CookResponse getHiredCooks() {
        CookResponse response = new CookResponse();
        response.setSuccess(false);
        response.setCooks(cookRepo.getAllHiredWithoutOrders());
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
            } else if (!cookOptional.get().getHired()) {
                response.setMessage("Cook with id = " + cookId + " was already fired");
            } else {
                cookOptional.get().setHired(false);
                cookRepo.save(cookOptional.get());
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
        } else {
            String st = "SELECT c.cook_id, c.f_name, c.l_name, c.is_hired, COUNT(ao.assigned_orders_order_id) " +
                    "FROM cook c JOIN  (SELECT ao.cook_cook_id, ao.assigned_orders_order_id FROM cook_assigned_orders ao) ao " +
                    "ON c.cook_id = ao.cook_cook_id " +
                    "GROUP BY c.cook_id " +
                    "ORDER BY COUNT(ao.assigned_orders_order_id) DESC LIMIT ?";
            Query statement = em.createNativeQuery(st);
            statement.setParameter(1, limit);
            response.setCooks(statement.getResultList());
            response.setSuccess(true);
        }

        return response;
    }
}