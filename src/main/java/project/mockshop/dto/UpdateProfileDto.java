package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;
import project.mockshop.entity.Address;

@Builder
@Getter
public class UpdateProfileDto {
    private Long userId;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private Address address;
}