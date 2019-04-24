package model.repository;

import model.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Table, Integer> {
}
