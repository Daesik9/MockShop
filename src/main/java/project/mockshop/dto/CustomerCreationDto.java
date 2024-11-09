package project.mockshop.dto;

import jakarta.persistence.Embedded;
import lombok.*;
import project.mockshop.entity.AddressInfo;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CustomerCreationDto {
    private String loginId;
    private String name;
    private String password;
    private String phoneNumber;
    private String email;
    @Embedded
    private AddressInfo addressInfo;
}