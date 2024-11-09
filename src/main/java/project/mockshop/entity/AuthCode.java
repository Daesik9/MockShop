package project.mockshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthCode {
    @Id
    private String email;
    private String code;
    private LocalDateTime expirationTime;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }
}
