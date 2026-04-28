package cl.kagejin.taskapi.controller;

import cl.kagejin.taskapi.dto.ApiResponse;
import cl.kagejin.taskapi.dto.PagedResponse;
import cl.kagejin.taskapi.dto.TaskRequest;
import cl.kagejin.taskapi.dto.TaskResponse;
import cl.kagejin.taskapi.dto.TaskStatusRequest;
import cl.kagejin.taskapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "title", "completed", "createdAt", "updatedAt");
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ApiResponse<PagedResponse<TaskResponse>> getTasks(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false, name = "q") String queryText,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        int sanitizedPage = Math.max(page, 0);
        int sanitizedSize = Math.min(Math.max(size, 1), 100);
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        String sanitizedSortBy = ALLOWED_SORT_FIELDS.contains(sortBy) ? sortBy : "createdAt";
        Pageable pageable = PageRequest.of(sanitizedPage, sanitizedSize, Sort.by(direction, sanitizedSortBy));

        PagedResponse<TaskResponse> response = taskService.getTasks(completed, queryText, pageable);
        return new ApiResponse<>("Tareas obtenidas", response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse createdTask = taskService.createTask(taskRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Tarea creada", createdTask));
    }

    @GetMapping("/{id}")
    public ApiResponse<TaskResponse> getTaskById(@PathVariable Long id) {
        TaskResponse task = taskService.getTaskById(id);
        return new ApiResponse<>("Tarea obtenida", task);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ApiResponse<>("Tarea " + id + " eliminada", null);
    }

    @PutMapping("/{id}")
    public ApiResponse<TaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse updatedTask = taskService.updateTask(id, taskRequest);
        return new ApiResponse<>("Tarea actualizada", updatedTask);
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<TaskResponse> updateTaskStatus(@PathVariable Long id, @Valid @RequestBody TaskStatusRequest taskStatusRequest) {
        TaskResponse updatedTask = taskService.updateTaskStatus(id, taskStatusRequest);
        return new ApiResponse<>("Estado de tarea actualizado", updatedTask);
    }
}
