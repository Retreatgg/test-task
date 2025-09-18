package com.axelor.apps.testtask.mapper;

import com.axelor.apps.testtask.dto.TaskCreateDto;
import com.axelor.apps.testtask.dto.TaskDto;
import com.axelor.apps.testtask.dto.TaskUpdateDto;
import com.axelor.apps.testtask.model.Task;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto toDto(Task task);
    List<TaskDto> toDtoList(List<Task> tasks);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "updatedDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "isDeleted", constant = "false")
    Task toEntityFromCreateDto(TaskCreateDto taskCreateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "updatedDate", expression = "java(java.time.Instant.now())")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromDto(TaskUpdateDto dto, @MappingTarget Task taskToUpdate);

}
