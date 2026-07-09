package space.rodrigorocha.user_service.model.dto;

import jakarta.validation.constraints.NotBlank;

public record  RoleAssignmentRequestRecord (
        @NotBlank(message = "roleName is required")
        String roleName
) {
}
