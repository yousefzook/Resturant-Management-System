package model.repository;

import model.OrderState;
import model.entity.Cook;
import model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findOrderByState(OrderState state);

    List<Order> findOrderByCookAndState(Cook cook, OrderState state);
}
