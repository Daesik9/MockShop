package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;
import project.mockshop.entity.AddressInfo;

@Builder
@Getter
public class CustomerDto {
    private Long id;
    private String loginId;
    private String name;
    private String password;
    private String phoneNumber;
    private String email;
    private AddressInfo addressInfo;
    private int point;
    private boolean isDeleted;
}