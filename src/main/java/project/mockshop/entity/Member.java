package project.mockshop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import project.mockshop.validator.CustomerValidator;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//@Builder
//@SuperBuilder
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

    @Column(insertable=false, updatable=false)
    private String role;

    public static class MemberBuilder {
        private Long id;
        private String loginId;
        private String name;
        private String password;
        private String phoneNumber;
        private String email;
        private Address address;
        private boolean isDeleted;
        private String role;

        public MemberBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MemberBuilder loginId(String loginId) {
            this.loginId = loginId;
            return this;
        }

        public MemberBuilder role(String role) {
            this.role = role;
            return this;
        }

        public Member build() {
            return new Member(id, loginId, name, password, phoneNumber, email, address, isDeleted, role);
        }
    }

    public static MemberBuilder memberBuilder() {
        return new MemberBuilder();
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

    public void changeRole(String role) {
        this.role = role;
    }
}