package project.mockshop.entity;

import jakarta.persistence.*;
import lombok.*;
import project.mockshop.validator.CustomerValidator;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    private String loginId;
    private String name;
    private String password;
    private String phoneNumber;
    private String email;

    @Embedded
    private Address address;
    private int point;
    private boolean isDeleted;

    public void changeId(Long id) {
        this.id = id;
    }

    public void changeName(String name) {
        CustomerValidator.validateName(name);
        this.name = name;
    }

    public void changeIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void changePassword(String password) {
        CustomerValidator.validatePassword(password);
        this.password = password;
    }

    public void changePhoneNumber(String phoneNumber) {
        CustomerValidator.validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public void changeEmail(String email) {
        CustomerValidator.validateEmail(email);
        this.email = email;
    }

    public void changeAddress(Address address) {
        this.address = address;
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