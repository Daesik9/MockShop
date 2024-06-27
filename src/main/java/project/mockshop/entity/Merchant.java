package project.mockshop.entity;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Merchant extends Member {
    private String storeName;

    @Builder
    public Merchant(Long id, String loginId, String name, String password, String phoneNumber,
                    String email, Address address, boolean isDeleted, String storeName) {
        super(id, loginId, name, password, phoneNumber, email, address, isDeleted);
        this.storeName = storeName;
    }
}
