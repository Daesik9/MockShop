package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthCodeDto {
    private String authCode;
    private String email;
}
