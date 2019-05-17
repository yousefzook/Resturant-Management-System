package model.repository;

import model.OrderState;
import model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "SELECT * FROM order_details WHERE state = ?", nativeQuery = true)
    List<Order> findAllByOrderState(OrderState orderState);
}
