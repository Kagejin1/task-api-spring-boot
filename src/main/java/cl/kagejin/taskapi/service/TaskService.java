package cl.kagejin.taskapi.service;

import cl.kagejin.taskapi.dto.TaskRequest;
import cl.kagejin.taskapi.dto.TaskResponse;
import cl.kagejin.taskapi.exception.TaskNotFoundException;
import cl.kagejin.taskapi.model.Task;
import cl.kagejin.taskapi.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskResponse::fromTask)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id) {
        return TaskResponse.fromTask(findTaskById(id));
    }

    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest) {
        Task task = new Task();
        applyRequest(task, taskRequest);
        Task savedTask = taskRepository.save(task);
        return TaskResponse.fromTask(savedTask);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest taskRequest) {
        Task task = findTaskById(id);
        applyRequest(task, taskRequest);
        Task savedTask = taskRepository.save(task);
        return TaskResponse.fromTask(savedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = findTaskById(id);
        taskRepository.delete(task);
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    private void applyRequest(Task task, TaskRequest taskRequest) {
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setCompleted(taskRequest.isCompleted());
    }
}
