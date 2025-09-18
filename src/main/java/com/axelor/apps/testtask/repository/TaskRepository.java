package com.axelor.apps.testtask.repository;

import com.axelor.apps.testtask.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndIsDeletedFalse(Long id);
}