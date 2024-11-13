package project.mockshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginDto {
    private Long id;
    //login request때 이미 loginId를 프론트에서 받아오니 조회할 필요x
    private String password;
    private String role;
}
