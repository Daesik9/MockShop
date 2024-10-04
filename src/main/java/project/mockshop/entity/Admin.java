package project.mockshop.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@DiscriminatorValue("ADMIN")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@SuperBuilder
public class Admin extends Member {

    @Builder
    public Admin(Long id, String loginId, String name, String password, String phoneNumber,
                    String email, Address address, boolean isDeleted, String role) {
        super(id, loginId, name, password, phoneNumber, email, address, isDeleted, role);
    }

    ///TODO: 필드들 validation check
}