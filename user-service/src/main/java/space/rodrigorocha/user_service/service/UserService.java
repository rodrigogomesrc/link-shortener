package space.rodrigorocha.user_service.service;

import space.rodrigorocha.user_service.model.dto.UserCreationRequestRecord;

public interface UserService {
    String createUser(UserCreationRequestRecord request);
    void deactivateUser(String userId);
    void assignRoleToUser(String userId, String roleName);
    void removeRoleFromUser(String userId, String roleName);
}
