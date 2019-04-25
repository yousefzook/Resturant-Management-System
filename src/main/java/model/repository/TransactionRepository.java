package model.repository;

import model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT COALESCE(SUM(t.amountFin),0) FROM Transaction t WHERE t.date BETWEEN :fromDate AND :toDate")
    Float getTotalIncome(Date fromDate, Date toDate);
}
