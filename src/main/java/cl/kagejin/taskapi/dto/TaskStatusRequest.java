package cl.kagejin.taskapi.dto;

import jakarta.validation.constraints.NotNull;

public class TaskStatusRequest {

    @NotNull(message = "El estado completed es obligatorio")
    private Boolean completed;

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
