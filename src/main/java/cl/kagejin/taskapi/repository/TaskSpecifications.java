package cl.kagejin.taskapi.repository;

import cl.kagejin.taskapi.model.Task;
import org.springframework.data.jpa.domain.Specification;

public final class TaskSpecifications {

    private TaskSpecifications() {
    }

    public static Specification<Task> hasCompleted(Boolean completed) {
        return (root, query, criteriaBuilder) ->
                completed == null ? null : criteriaBuilder.equal(root.get("completed"), completed);
    }

    public static Specification<Task> containsText(String text) {
        return (root, query, criteriaBuilder) -> {
            if (text == null || text.isBlank()) {
                return null;
            }

            String likeText = "%" + text.trim().toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likeText),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likeText)
            );
        };
    }
}
