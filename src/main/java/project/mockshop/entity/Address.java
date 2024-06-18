package project.mockshop.entity;

public class Address {
    private final String city;
    private final String street;
    private final String zipcode;

    public Address(String city, String street, String zipcode) {
        validateZipcode(zipcode);
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }

    private void validateZipcode(String zipcode) {
        for (char c : zipcode.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("우편번호는 숫자로만 구성되어야 합니다.");
            }
        }
    }
}