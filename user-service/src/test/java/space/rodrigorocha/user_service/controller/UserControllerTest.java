package space.rodrigorocha.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import space.rodrigorocha.user_service.model.dto.RoleAssignmentRequestRecord;
import space.rodrigorocha.user_service.model.dto.UserCreationRequestRecord;
import space.rodrigorocha.user_service.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    private static final String USER_ID = "user-12345";

    @Test
    void createUser_Success() throws Exception {
        // Arrange
        UserCreationRequestRecord request = new UserCreationRequestRecord(
                "test@test.com", "password123", "John", "Doe");

        when(userService.createUser(any(UserCreationRequestRecord.class))).thenReturn(USER_ID);

        // Act & Assert
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(USER_ID));

        verify(userService).createUser(any(UserCreationRequestRecord.class));
    }

    @Test
    void createUser_BadRequest_WhenInvalidData() throws Exception {
        // Arrange: invalid data
        UserCreationRequestRecord request = new UserCreationRequestRecord(
                null, "short", "John", "Doe");

        // Act & Assert
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void deactivateUser_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(patch("/{userId}/deactivate", USER_ID))
                .andExpect(status().isNoContent());

        verify(userService).deactivateUser(USER_ID);
    }

    @Test
    void assignRole_Success() throws Exception {
        // Arrange
        String roleName = "ROLE_ADMIN";
        RoleAssignmentRequestRecord request = new RoleAssignmentRequestRecord(roleName);

        // Act & Assert
        mockMvc.perform(post("/{userId}/roles", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(userService).assignRoleToUser(eq(USER_ID), eq(roleName));
    }

    @Test
    void removeRole_Success() throws Exception {
        // Arrange
        String roleName = "ROLE_ADMIN";

        // Act & Assert
        mockMvc.perform(delete("/{userId}/roles/{roleName}", USER_ID, roleName))
                .andExpect(status().isNoContent());

        verify(userService).removeRoleFromUser(USER_ID, roleName);
    }
}