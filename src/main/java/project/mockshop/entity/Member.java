package project.mockshop.entity;

import jakarta.persistence.*;
import lombok.*;
import project.mockshop.validator.CustomerValidator;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

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
}