package cl.kagejin.taskapi.dto;

import cl.kagejin.taskapi.model.Task;

import java.time.Instant;

public class TaskResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final boolean completed;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Long version;

    public TaskResponse(Long id, String title, String description, boolean completed, Instant createdAt, Instant updatedAt, Long version) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public static TaskResponse fromTask(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getVersion()
        );
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }
}
