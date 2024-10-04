package project.mockshop.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.mockshop.validator.CustomerValidator;

@Entity
@DiscriminatorValue("CUSTOMER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends Member {
    private int point;

    public Customer(Long id, String loginId, String name, String password, String phoneNumber,
                    String email, Address address, boolean isDeleted, int point, String role) {
        super(id, loginId, name, password, phoneNumber, email, address, isDeleted, role);
        this.point = point;
    }


    public static class CustomerBuilder {
        private Long id;
        private String loginId;
        private String name;
        private String password;
        private String phoneNumber;
        private String email;
        private Address address;
        private boolean isDeleted;
        private int point;
        private String role;

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

        public CustomerBuilder address(Address address) {
            this.address = address;
            return this;
        }

        public CustomerBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CustomerBuilder isDeleted(boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this;
        }

        public CustomerBuilder role(String role) {
            this.role = role;
            return this;
        }

        public Customer build() {
            return new Customer(id, loginId, name, password, phoneNumber, email, address, isDeleted, point, role);
        }
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }
}