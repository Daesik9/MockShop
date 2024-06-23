package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;
import project.mockshop.entity.Address;

@Builder
@Getter
public class CustomerDto {
    private Long id;
    private String loginId;
    private String name;
    private String password;
    private String phoneNumber;
    private String email;
    private Address address;
    private int point;
    private boolean isDeleted;
}