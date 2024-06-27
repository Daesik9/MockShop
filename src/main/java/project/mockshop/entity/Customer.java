package project.mockshop.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import project.mockshop.validator.CustomerValidator;

@Entity
@DiscriminatorValue("CUSTOMER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends Member {
    private int point;

    @Builder
    public Customer(Long id, String loginId, String name, String password, String phoneNumber,
                    String email, Address address, boolean isDeleted, int point) {
        super(id, loginId, name, password, phoneNumber, email, address, isDeleted);
        this.point = point;
    }


    public static class CustomerBuilder {
        public CustomerBuilder loginId(String loginId) {
            CustomerValidator.validateLoginId(loginId);
            this.loginId = loginId;
            return this;
        }

        public CustomerBuilder password(String password) {
            CustomerValidator.validatePassword(password);
            this.password = password;
            return this;
        }

        public CustomerBuilder name(String name) {
            CustomerValidator.validateName(name);
            this.name = name;
            return this;
        }

        public CustomerBuilder phoneNumber(String phoneNumber) {
            CustomerValidator.validatePhoneNumber(phoneNumber);
            this.phoneNumber = phoneNumber;
            return this;
        }

        public CustomerBuilder email(String email) {
            CustomerValidator.validateEmail(email);
            this.email = email;
            return this;
        }

        public CustomerBuilder point(int point) {
            CustomerValidator.validatePoint(point);
            this.point = point;
            return this;
        }
    }

}
