package project.mockshop.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    public Address(String city, String street, String zipcode) {
        validateZipcode(zipcode);
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    private void validateZipcode(String zipcode) {
        for (char c : zipcode.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("우편번호는 숫자로만 구성되어야 합니다.");
            }
        }
    }
}