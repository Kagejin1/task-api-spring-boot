package cl.kagejin.taskapi.repository;

import cl.kagejin.taskapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}