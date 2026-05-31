package space.rodrigorocha.user_service.service.impl;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.rodrigorocha.user_service.exception.KeycloakIntegrationException;
import space.rodrigorocha.user_service.exception.ResourceNotFoundException;
import space.rodrigorocha.user_service.exception.UserAlreadyExistsException;
import space.rodrigorocha.user_service.model.dto.UserCreationRequestRecord;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private Keycloak keycloak;

    @Mock
    private RealmResource realmResource;

    @Mock
    private UsersResource usersResource;

    @Mock
    private UserResource userResource;

    @Mock
    private RolesResource rolesResource;

    @Mock
    private RoleResource roleResource;

    @Mock
    private RoleMappingResource roleMappingResource;

    @Mock
    private RoleScopeResource roleScopeResource;

    @Mock
    private Response response;

    private UserServiceImpl userService;

    private static final String REALM = "test-realm";
    private static final String USER_ID = "user-12345";
    private static final String VALID_ROLE = "ROLE_FREE";

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(keycloak, REALM);
    }

    private void mockRealm() {
        when(keycloak.realm(REALM)).thenReturn(realmResource);
    }

    private void mockUsersResource() {
        mockRealm();
        when(realmResource.users()).thenReturn(usersResource);
    }

    private void mockUserResource() {
        mockUsersResource();
        when(usersResource.get(anyString())).thenReturn(userResource);
    }

    private void mockRoleAssignment() {
        when(realmResource.roles()).thenReturn(rolesResource);
        when(rolesResource.get(anyString())).thenReturn(roleResource);
        when(roleResource.toRepresentation()).thenReturn(new RoleRepresentation());
        when(userResource.roles()).thenReturn(roleMappingResource);
        when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
    }

    // ==========================================
    // Create user
    // ==========================================

    @Test
    void createUser_Success() throws Exception {
        // Arrange
        UserCreationRequestRecord request = new UserCreationRequestRecord(
                "test@test.com", "password123", "John", "Doe");

        mockUserResource();
        mockRoleAssignment();

        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(201);
        when(response.getLocation()).thenReturn(new URI("http://localhost/auth/admin/realms/test-realm/users/" + USER_ID));

        // Act
        String resultId = userService.createUser(request);

        // Assert
        assertEquals(USER_ID, resultId);

        ArgumentCaptor<UserRepresentation> userCaptor = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(usersResource).create(userCaptor.capture());
        UserRepresentation capturedUser = userCaptor.getValue();
        assertEquals("test@test.com", capturedUser.getUsername());
        assertEquals("test@test.com", capturedUser.getEmail());
        assertTrue(capturedUser.isEnabled());
        assertFalse(capturedUser.isEmailVerified());

        ArgumentCaptor<CredentialRepresentation> credentialCaptor = ArgumentCaptor.forClass(CredentialRepresentation.class);
        verify(userResource).resetPassword(credentialCaptor.capture());
        assertEquals("password123", credentialCaptor.getValue().getValue());
        assertEquals(CredentialRepresentation.PASSWORD, credentialCaptor.getValue().getType());
        assertFalse(credentialCaptor.getValue().isTemporary());

        verify(roleScopeResource).add(anyList());
    }

    @Test
    void createUser_UserAlreadyExists() {
        // Arrange
        UserCreationRequestRecord request = new UserCreationRequestRecord(
                "test@test.com", "John", "Doe", "password123");

        mockUsersResource();
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(409);

        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createUser(request)
        );
        assertTrue(exception.getMessage().contains("test@test.com"));
    }

    @Test
    void createUser_KeycloakIntegrationException() {
        // Arrange
        UserCreationRequestRecord request = new UserCreationRequestRecord(
                "test@test.com", "John", "Doe", "password123");

        mockUsersResource();
        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);
        when(response.getStatus()).thenReturn(500);

        // Act & Assert
        KeycloakIntegrationException exception = assertThrows(
                KeycloakIntegrationException.class,
                () -> userService.createUser(request)
        );
        assertTrue(exception.getMessage().contains("500"));
    }

    @Test
    void createUser_UnexpectedException() {
        // Arrange
        UserCreationRequestRecord request = new UserCreationRequestRecord(
                "test@test.com", "John", "Doe", "password123");

        mockUsersResource();
        when(usersResource.create(any(UserRepresentation.class))).thenThrow(new RuntimeException("Connection failed"));

        // Act & Assert
        KeycloakIntegrationException exception = assertThrows(
                KeycloakIntegrationException.class,
                () -> userService.createUser(request)
        );
        assertTrue(exception.getMessage().contains("Unexpected failure while communicating with Keycloak"));
    }

    // ==========================================
    // Deactivate User
    // ==========================================

    @Test
    void deactivateUser_Success() {
        // Arrange
        mockUserResource();
        UserRepresentation mockRepresentation = new UserRepresentation();
        mockRepresentation.setEnabled(true);
        when(userResource.toRepresentation()).thenReturn(mockRepresentation);

        // Act
        userService.deactivateUser(USER_ID);

        // Assert
        ArgumentCaptor<UserRepresentation> captor = ArgumentCaptor.forClass(UserRepresentation.class);
        verify(userResource).update(captor.capture());
        assertFalse(captor.getValue().isEnabled());
    }

    @Test
    void deactivateUser_UserNotFound() {
        // Arrange
        mockUsersResource();
        when(usersResource.get(USER_ID)).thenThrow(new NotFoundException());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.deactivateUser(USER_ID)
        );
        assertTrue(exception.getMessage().contains(USER_ID));
    }

    @Test
    void deactivateUser_KeycloakIntegrationException() {
        // Arrange
        mockUsersResource();
        when(usersResource.get(USER_ID)).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        KeycloakIntegrationException exception = assertThrows(
                KeycloakIntegrationException.class,
                () -> userService.deactivateUser(USER_ID)
        );
        assertTrue(exception.getMessage().contains(USER_ID));
    }

    // ==========================================
    // AssignRoleToUser
    // ==========================================

    @Test
    void assignRoleToUser_Success() {
        // Arrange
        mockUserResource();
        mockRoleAssignment();

        // Act
        userService.assignRoleToUser(USER_ID, VALID_ROLE);

        // Assert
        ArgumentCaptor<List<RoleRepresentation>> roleListCaptor = ArgumentCaptor.forClass(List.class);
        verify(roleScopeResource).add(roleListCaptor.capture());
        assertEquals(1, roleListCaptor.getValue().size());
    }

    @Test
    void assignRoleToUser_InvalidRoleEnum() {
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.assignRoleToUser(USER_ID, "INVALID_ROLE_THAT_DOES_NOT_EXIST")
        );
        assertEquals("User or ROLE not found", exception.getMessage());
    }

    @Test
    void assignRoleToUser_NotFoundInKeycloak() {
        // Arrange
        mockRealm();
        when(realmResource.users()).thenThrow(new NotFoundException());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.assignRoleToUser(USER_ID, VALID_ROLE)
        );
        assertEquals("User or ROLE not found", exception.getMessage());
    }

    // ==========================================
    // RemoveRoleFromUser
    // ==========================================

    @Test
    void removeRoleFromUser_Success() {
        // Arrange
        mockUserResource();
        mockRoleAssignment();

        // Act
        userService.removeRoleFromUser(USER_ID, VALID_ROLE);

        // Assert
        ArgumentCaptor<List<RoleRepresentation>> roleListCaptor = ArgumentCaptor.forClass(List.class);
        verify(roleScopeResource).remove(roleListCaptor.capture());
        assertEquals(1, roleListCaptor.getValue().size());
    }

    @Test
    void removeRoleFromUser_InvalidRoleEnum() {
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.removeRoleFromUser(USER_ID, "NON_EXISTENT_ROLE")
        );
        assertEquals("User or ROLE not found", exception.getMessage());
    }

    @Test
    void removeRoleFromUser_KeycloakIntegrationException() {
        // Arrange
        mockRealm();
        when(realmResource.users()).thenThrow(new RuntimeException("Keycloak connection error"));

        // Act & Assert
        KeycloakIntegrationException exception = assertThrows(
                KeycloakIntegrationException.class,
                () -> userService.removeRoleFromUser(USER_ID, VALID_ROLE)
        );
        assertTrue(exception.getMessage().contains("Failure removing role"));
    }
}