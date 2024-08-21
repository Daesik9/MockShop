package project.mockshop.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;
}
