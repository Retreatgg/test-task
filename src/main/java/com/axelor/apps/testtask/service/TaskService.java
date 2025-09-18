package com.axelor.apps.testtask.service;

import com.axelor.apps.testtask.dto.TaskCreateDto;
import com.axelor.apps.testtask.dto.TaskDto;
import com.axelor.apps.testtask.dto.TaskUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    TaskDto getById(Long taskId);
    List<TaskDto> getAll();
    void deleteById(Long taskId);
    TaskDto create(TaskCreateDto taskCreateDto);
    TaskDto update(TaskUpdateDto taskUpdateDto, Long taskId);
}
