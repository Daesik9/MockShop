package project.mockshop.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor
@Getter
@ToString(of = {"address", "detailedAddress", "zonecode"})
public class AddressInfo {
    private String address;
    private String detailedAddress;
    private String zonecode;

    public AddressInfo(String address, String detailedAddress, String zonecode) {
        validateZonecode(zonecode);
        this.address = address;
        this.detailedAddress = detailedAddress;
        this.zonecode = zonecode;
    }

    private void validateZonecode(String zonecode) {
        for (char c : zonecode.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("우편번호는 숫자로만 구성되어야 합니다.");
            }
        }
    }
}