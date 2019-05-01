package model.repository;

import model.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {
    @Query(value = "SELECT * FROM dish_details WHERE IS_ACTIVE = TRUE ORDER BY rate DESC LIMIT ?", nativeQuery = true)
    List<Dish> getTopDishes(int limit);

    List<Dish> findAllByActive(Boolean isActive);

    List<Dish> findAllByIdInAndActiveTrue(List<Integer> ids);
}
