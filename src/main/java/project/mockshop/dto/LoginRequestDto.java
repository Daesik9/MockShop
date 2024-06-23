package project.mockshop.dto;

import lombok.*;

@Builder
@Getter
public class LoginRequestDto {
    private final String loginId;
    private final String password;
}
