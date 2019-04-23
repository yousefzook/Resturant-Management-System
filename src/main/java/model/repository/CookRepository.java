package model.repository;

import model.entity.Cook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CookRepository extends JpaRepository<Cook, Integer> {
    @Query(value = "SELECT new Cook(id, firstName, lastName, hired) FROM Cook")
    List<Cook> getAllWithoutOrders();

    @Query(value = "SELECT new Cook(id, firstName, lastName, hired) FROM Cook WHERE hired = true")
    List<Cook> getAllHiredWithoutOrders();
}
