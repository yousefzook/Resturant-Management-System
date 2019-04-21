package model.repository;

import model.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    @Query(value = "SELECT SUM(t.AMOUNT_FIN) FROM TRANSACTIONS t WHERE t.TRANS_DATE BETWEEN :fromDate AND :toDate", nativeQuery = true)
    Float getTotalIncome(Date fromDate, Date toDate);
}
