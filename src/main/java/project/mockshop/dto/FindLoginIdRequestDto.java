package project.mockshop.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindLoginIdRequestDto {
    private String phoneNumber;
}
