package com.axelor.apps.testtask.controller;

import com.axelor.apps.testtask.dto.TaskCreateDto;
import com.axelor.apps.testtask.dto.TaskDto;
import com.axelor.apps.testtask.dto.TaskUpdateDto;
import com.axelor.apps.testtask.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable(name = "id") Long taskId) {
        return ResponseEntity.ok(taskService.getById(taskId));
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks() {
        return ResponseEntity.ok(taskService.getAll());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") Long taskId) {
        taskService.deleteById(taskId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskCreateDto taskCreateDto) {
        TaskDto taskDto = taskService.create(taskCreateDto);
        return ResponseEntity.created(URI.create("/api/tasks/" + taskDto.getId())).body(taskDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<TaskDto> updateTask(
            @RequestBody TaskUpdateDto taskUpdateDto,
            @PathVariable(name = "id") Long taskId
    ) {
        TaskDto updatedTask = taskService.update(taskUpdateDto, taskId);
        return ResponseEntity.ok(updatedTask);
    }
}
