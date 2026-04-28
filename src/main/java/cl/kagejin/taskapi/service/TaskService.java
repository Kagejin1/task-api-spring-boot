package cl.kagejin.taskapi.service;

import cl.kagejin.taskapi.dto.PagedResponse;
import cl.kagejin.taskapi.dto.TaskRequest;
import cl.kagejin.taskapi.dto.TaskResponse;
import cl.kagejin.taskapi.dto.TaskStatusRequest;
import cl.kagejin.taskapi.exception.TaskNotFoundException;
import cl.kagejin.taskapi.model.Task;
import cl.kagejin.taskapi.repository.TaskRepository;
import cl.kagejin.taskapi.repository.TaskSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public PagedResponse<TaskResponse> getTasks(Boolean completed, String queryText, Pageable pageable) {
        Specification<Task> specification = Specification.where(TaskSpecifications.hasCompleted(completed))
                .and(TaskSpecifications.containsText(queryText));

        Page<TaskResponse> page = taskRepository.findAll(specification, pageable)
                .map(TaskResponse::fromTask);

        return PagedResponse.fromPage(page);
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
    public TaskResponse updateTaskStatus(Long id, TaskStatusRequest taskStatusRequest) {
        Task task = findTaskById(id);
        task.setCompleted(taskStatusRequest.getCompleted());
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
        task.setTitle(taskRequest.getTitle().trim());
        task.setDescription(taskRequest.getDescription());
        task.setCompleted(taskRequest.isCompleted());
    }
}
