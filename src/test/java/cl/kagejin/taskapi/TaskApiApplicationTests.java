package cl.kagejin.taskapi;

import cl.kagejin.taskapi.model.Task;
import cl.kagejin.taskapi.repository.TaskRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskApiApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void cleanDatabase() {
        taskRepository.deleteAll();
    }

    @Test
    void createAndGetTask() throws Exception {
        String createPayload = """
                {
                  "title": "Primera tarea",
                  "description": "Descripcion inicial",
                  "completed": false
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Tarea creada"))
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.title").value("Primera tarea"));

        Assertions.assertEquals(1, taskRepository.count());
        long taskId = taskRepository.findAll().get(0).getId();

        mockMvc.perform(get("/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value((int) taskId))
                .andExpect(jsonPath("$.title").value("Primera tarea"))
                .andExpect(jsonPath("$.description").value("Descripcion inicial"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void updateTask() throws Exception {
        Task task = new Task();
        task.setTitle("Antes");
        task.setDescription("Pendiente");
        task.setCompleted(false);
        task = taskRepository.save(task);

        String updatePayload = """
                {
                  "title": "Despues",
                  "description": "Hecha",
                  "completed": true
                }
                """;

        mockMvc.perform(put("/tasks/{id}", task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tarea actualizada"))
                .andExpect(jsonPath("$.data.id").value(task.getId().intValue()))
                .andExpect(jsonPath("$.data.title").value("Despues"))
                .andExpect(jsonPath("$.data.description").value("Hecha"))
                .andExpect(jsonPath("$.data.completed").value(true));
    }

    @Test
    void deleteTask() throws Exception {
        Task task = new Task();
        task.setTitle("Eliminar");
        task.setDescription("Temporal");
        task.setCompleted(false);
        task = taskRepository.save(task);

        mockMvc.perform(delete("/tasks/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Tarea " + task.getId() + " eliminada"))
                .andExpect(jsonPath("$.data").value(Matchers.nullValue()));

        mockMvc.perform(get("/tasks/{id}", task.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void validationErrorWhenTitleIsBlank() throws Exception {
        String invalidPayload = """
                {
                  "title": " ",
                  "description": "Sin titulo",
                  "completed": false
                }
                """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Datos de entrada invalidos"))
                .andExpect(jsonPath("$.data.title").value("Title is required"));
    }

    @Test
    void returnNotFoundForUnknownTask() throws Exception {
        mockMvc.perform(get("/tasks/{id}", 99999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task no encontrada con id: 99999"));
    }

}
