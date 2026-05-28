package space.rodrigorocha.user_service.service.impl;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import space.rodrigorocha.user_service.exception.KeycloakIntegrationException;
import space.rodrigorocha.user_service.exception.ResourceNotFoundException;
import space.rodrigorocha.user_service.exception.UserAlreadyExistsException;
import space.rodrigorocha.user_service.model.dto.UserCreationRequestRecord;
import space.rodrigorocha.user_service.model.enums.RolesEnum;
import space.rodrigorocha.user_service.service.UserService;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private final Keycloak keycloak;
    private final String realm;

    public UserServiceImpl(Keycloak keycloak,
                           @Value("${keycloak.realm}") String realm) {

        this.keycloak = keycloak;
        this.realm = realm;
    }

    @Override
    public String createUser(UserCreationRequestRecord request) {
        RealmResource realmResource = keycloak.realm(realm);

        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.email());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEnabled(true);
        user.setEmailVerified(false);

        try (Response response = realmResource.users().create(user)) {
            if (response.getStatus() == 201) {
                String path = response.getLocation().getPath();
                String userId = path.substring(path.lastIndexOf('/') + 1);

                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setTemporary(false);
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(request.password());

                realmResource.users().get(userId).resetPassword(credential);

                assignRoleToUser(userId, String.valueOf(RolesEnum.ROLE_FREE));

                return userId;
            } else if (response.getStatus() == 409) {
                throw new UserAlreadyExistsException("User already exists with email: " + request.email());
            } else {
                throw new KeycloakIntegrationException("Error creating user. Keycloak status code: " + response.getStatus(), null);
            }
        } catch (UserAlreadyExistsException e) {
            throw e;
        } catch (Exception e) {
            throw new KeycloakIntegrationException("Unexpected failure communication with Keycloak.", e);
        }
    }

    @Override
    public void deactivateUser(String userId) {
        try {
            RealmResource realmResource = keycloak.realm(realm);
            UserResource userResource = realmResource.users().get(userId);

            UserRepresentation user = userResource.toRepresentation();
            user.setEnabled(false);

            userResource.update(user);
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        } catch (Exception e) {
            throw new KeycloakIntegrationException("Failure deactivating user with ID: " + userId, e);
        }
    }

    @Override
    public void assignRoleToUser(String userId, String roleName) {

        try {
            RolesEnum role = RolesEnum.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Usuario ou Role nao encontrados.");
        }

        try {
            RealmResource realmResource = keycloak.realm(realm);
            UserResource userResource = realmResource.users().get(userId);

            RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(role));

        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("User or ROLE not found");
        } catch (Exception e) {
            throw new KeycloakIntegrationException("Failure attributing role to user with ID: " + userId, e);
        }

    }

    @Override
    public void removeRoleFromUser(String userId, String roleName) {

        try {
            RolesEnum role = RolesEnum.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Usuario ou Role nao encontrados.");
        }

        try {
            RealmResource realmResource = keycloak.realm(realm);
            UserResource userResource = realmResource.users().get(userId);

            RoleRepresentation role = realmResource.roles().get(roleName).toRepresentation();
            userResource.roles().realmLevel().remove(Collections.singletonList(role));
        } catch (NotFoundException e) {
            throw new ResourceNotFoundException("User or ROLE not found");
        } catch (Exception e) {
            throw new KeycloakIntegrationException("Failure attributing role to user with ID: " + userId, e);
        }

    }
}
