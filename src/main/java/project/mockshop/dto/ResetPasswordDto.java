package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResetPasswordDto {
    private String email;
    private String password;
}
