package com.example.repository;


import com.example.model.Task;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "Select * from tasks where user_id = :userId", nativeQuery = true)
    List<Task> findByUserId(Integer userId);

    @Query(value = "SELECT * FROM tasks WHERE created_date < ?1 AND completed = false", nativeQuery = true)
    List<Task> findByCreatedDateBeforeAndStatusNotDone(LocalDateTime createdDate);
}
