package model.repository;

import model.entity.Cook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CookRepository extends JpaRepository<Cook, Integer> {
    @Query(value = "SELECT cook_id, f_name, l_name, is_hired FROM cook", nativeQuery = true)
    List<Cook> getAllWithoutOrders();

    @Query(value =
            "SELECT cook_id, f_name, l_name, is_hired FROM cook GROUP BY cook_id ORDER BY COUNT(*) LIMIT :limit", nativeQuery = true)
    List<Cook> getTopCooks(int limit);
}
