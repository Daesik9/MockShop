package project.mockshop.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EventRewardDto {
    private Long couponId;
    private int count;
}
