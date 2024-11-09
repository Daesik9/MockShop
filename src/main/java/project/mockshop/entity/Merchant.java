package project.mockshop.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("MERCHANT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@SuperBuilder
public class Merchant extends Member {
    private String storeName;

    @Builder
    public Merchant(Long id, String loginId, String name, String password, String phoneNumber,
                    String email, AddressInfo addressInfo, boolean isDeleted, String storeName, String role) {
        super(id, loginId, name, password, phoneNumber, email, addressInfo, isDeleted, role);
        this.storeName = storeName;
    }

    ///TODO: 필드들 validation check
}