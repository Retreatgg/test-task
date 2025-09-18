package com.axelor.apps.testtask.service.impl;

import com.axelor.apps.testtask.dto.TaskCreateDto;
import com.axelor.apps.testtask.dto.TaskDto;
import com.axelor.apps.testtask.dto.TaskUpdateDto;
import com.axelor.apps.testtask.exception.TaskNotFoundException;
import com.axelor.apps.testtask.mapper.TaskMapper;
import com.axelor.apps.testtask.model.Task;
import com.axelor.apps.testtask.model.User;
import com.axelor.apps.testtask.repository.TaskRepository;
import com.axelor.apps.testtask.service.AuthService;
import com.axelor.apps.testtask.service.ExternalApiService;
import com.axelor.apps.testtask.service.MailService;
import com.axelor.apps.testtask.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final ExternalApiService externalApiService;
    private final TaskRepository taskRepository;
    private final MailService mailService;
    private final AuthService authService;
    private final TaskMapper taskMapper;

    @Override
    public TaskDto getById(Long taskId) {
        log.info("Запрос на получение задачи по ID: {}", taskId);
        Task task = findById(taskId);
        return taskMapper.toDto(task);
    }

    @Override
    @Cacheable("tasks")
    public List<TaskDto> getAll() {
        log.info("Запрос на получение всех задач");
        externalApiService.fetchToExternalApi();

        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toDtoList(tasks);
    }

    @Transactional
    @Override
    @CacheEvict(value = "tasks", allEntries = true)
    public void deleteById(Long taskId) {
        log.info("Удаление задачи по ID: {}", taskId);
        Task task = findById(taskId);
        task.setIsDeleted(true);
        taskRepository.save(task);
    }

    @Transactional
    @Override
    @CacheEvict(value = "tasks", allEntries = true)
    public TaskDto create(TaskCreateDto taskCreateDto) {
        log.info("Создание задачи с заголовком: {}", taskCreateDto.getTaskName());
        Task task = taskMapper.toEntityFromCreateDto(taskCreateDto);
        Task savedTask = taskRepository.save(task);
        log.info("Информация о созданной задаче: {}", savedTask);

        User currentUser = authService.getCurrentUser();

        mailService.sendEmail(currentUser.getEmail(), savedTask);
        return taskMapper.toDto(savedTask);
    }

    @Transactional
    @Override
    @CacheEvict(value = "tasks", allEntries = true)
    public TaskDto update(TaskUpdateDto taskUpdateDto, Long taskId) {
        log.info("Обновление задачи по ID: {}", taskId);
        Task existingTask = findById(taskId);
        taskMapper.updateTaskFromDto(taskUpdateDto, existingTask);
        Task updatedTaskFromDatabase = taskRepository.save(existingTask);
        log.info("Обновленная задача: {}", updatedTaskFromDatabase);
        return taskMapper.toDto(updatedTaskFromDatabase);
    }

    private Task findById(Long taskId) {
        return taskRepository.findByIdAndIsDeletedFalse(taskId)
                .orElseThrow(
                        () -> new TaskNotFoundException("Задача по ID: " + taskId + " не найдена.")
                );
    }


}
