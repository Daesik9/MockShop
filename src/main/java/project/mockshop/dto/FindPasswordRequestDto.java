package project.mockshop.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPasswordRequestDto {
    private String loginId;
    private String phoneNumber;
}
