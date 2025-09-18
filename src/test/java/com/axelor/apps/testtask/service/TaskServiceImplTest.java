package com.axelor.apps.testtask.service;

import com.axelor.apps.testtask.dto.TaskCreateDto;
import com.axelor.apps.testtask.dto.TaskDto;
import com.axelor.apps.testtask.dto.TaskUpdateDto;
import com.axelor.apps.testtask.exception.TaskNotFoundException;
import com.axelor.apps.testtask.mapper.TaskMapper;
import com.axelor.apps.testtask.model.Task;
import com.axelor.apps.testtask.model.User;
import com.axelor.apps.testtask.repository.TaskRepository;
import com.axelor.apps.testtask.service.impl.ExternalApiServiceImpl;
import com.axelor.apps.testtask.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ExternalApiServiceImpl externalApiService;
    @Mock
    private MailService mailService;
    @Mock
    private TaskMapper taskMapper;
    @Mock
    private AuthService authService;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    @DisplayName("getById | Должен вернуть TaskDto, когда задача существует")
    void getById_whenTaskExists_shouldReturnTaskDto() {
        Long taskId = 1L;

        Task task = Task.builder()
                .id(taskId)
                .taskName("Тестовая задача")
                .isDeleted(false)
                .build();

        TaskDto expectedDto = TaskDto.builder()
                .id(taskId)
                .taskName("Тестовая задача")
                .build();

        when(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(expectedDto);

        TaskDto actualDto = taskService.getById(taskId);

        assertNotNull(actualDto);
        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getTaskName(), actualDto.getTaskName());

        verify(taskRepository, times(1)).findByIdAndIsDeletedFalse(taskId);
        verify(taskMapper, times(1)).toDto(task);
    }

    @Test
    @DisplayName("getById | Должен выбросить TaskNotFoundException, когда задача не найдена")
    void getById_whenTaskNotFound_shouldThrowException() {
        long taskId = 99L;
        when(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getById(taskId);
        });

        verify(taskMapper, never()).toDto(any(Task.class));
    }

    @Test
    @DisplayName("getAll | Должен вернуть список задач и вызвать внешнее API")
    void getAll_shouldReturnTaskListAndFetchApi() {
        Long taskId = 1L;
        Task task = Task.builder()
                .id(taskId)
                .isDeleted(false)
                .build();
        List<Task> taskList = List.of(task);

        TaskDto dto = TaskDto.builder()
                .id(taskId)
                .build();

        List<TaskDto> dtoList = List.of(dto);

        when(taskRepository.findAll()).thenReturn(taskList);
        when(taskMapper.toDtoList(taskList)).thenReturn(dtoList);
        doNothing().when(externalApiService).fetchToExternalApi();

        List<TaskDto> result = taskService.getAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(dto.getId(), result.get(0).getId());

        verify(externalApiService, times(1)).fetchToExternalApi();
        verify(taskRepository, times(1)).findAll();
        verify(taskMapper, times(1)).toDtoList(taskList);
    }

    @Test
    @DisplayName("create | Должен сохранить задачу и отправить email")
    void create_shouldSaveTaskAndSendEmail() {
        Long taskId = 1L;
        TaskCreateDto createDto = TaskCreateDto.builder()
                .taskName("Новая задача")
                .build();

        User currentUser = User.builder()
                .id(1L)
                .email("test@email.com")
                .build();

        Task taskToSave = new Task();
        Task savedTask = new Task();
        savedTask.setId(taskId);

        TaskDto expectedDto = new TaskDto();
        expectedDto.setId(taskId);

        when(taskMapper.toEntityFromCreateDto(createDto)).thenReturn(taskToSave);
        when(taskRepository.save(taskToSave)).thenReturn(savedTask);
        when(authService.getCurrentUser()).thenReturn(currentUser);
        doNothing().when(mailService).sendEmail(anyString(), any(Task.class));
        when(taskMapper.toDto(savedTask)).thenReturn(expectedDto);

        TaskDto resultDto = taskService.create(createDto);

        assertNotNull(resultDto);
        assertEquals(expectedDto.getId(), resultDto.getId());

        verify(taskRepository, times(1)).save(taskToSave);
        verify(mailService, times(1)).sendEmail(currentUser.getEmail(), savedTask);
    }

    @Test
    @DisplayName("update | Должен найти задачу, вызвать маппер для обновления и сохранить")
    void update_shouldFindAndUpdateTask() {
        Long taskId = 1L;
        TaskUpdateDto updateDto = TaskUpdateDto.builder()
                .taskName("Новое имя")
                .build();


        Task existingTask = Task.builder()
                .id(taskId)
                .taskName("Старое имя")
                .isDeleted(false)
                .build();


        Task updatedTaskFromDb = Task.builder()
                .id(taskId)
                .taskName("Новое имя")
                .isDeleted(false)
                .build();

        TaskDto expectedDto = TaskDto.builder()
                .id(taskId)
                .taskName("Новое имя")
                .build();

        when(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(Optional.of(existingTask));
        doNothing().when(taskMapper).updateTaskFromDto(updateDto, existingTask);
        when(taskRepository.save(existingTask)).thenReturn(updatedTaskFromDb);
        when(taskMapper.toDto(updatedTaskFromDb)).thenReturn(expectedDto);

        TaskDto resultDto = taskService.update(updateDto, taskId);

        assertNotNull(resultDto);
        assertEquals(expectedDto.getId(), resultDto.getId());

        verify(taskRepository, times(1)).findByIdAndIsDeletedFalse(taskId);
        verify(taskMapper, times(1)).updateTaskFromDto(updateDto, existingTask);
        verify(taskRepository, times(1)).save(existingTask);
        verify(taskMapper, times(1)).toDto(updatedTaskFromDb);
    }

    @Test
    @DisplayName("deleteById | Должен установить флаг isDeleted и сохранить задачу")
    void deleteById_shouldSetDeletedFlagAndSave() {
        Long taskId = 1L;

        Task task = Task.builder()
                .id(taskId)
                .isDeleted(false)
                .build();

        when(taskRepository.findByIdAndIsDeletedFalse(taskId)).thenReturn(Optional.of(task));
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        taskService.deleteById(taskId);

        verify(taskRepository, times(1)).save(taskCaptor.capture());
        Task savedTask = taskCaptor.getValue();
        assertTrue(savedTask.getIsDeleted());
    }
}
