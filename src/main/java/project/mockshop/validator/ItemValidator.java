package project.mockshop.validator;

import org.springframework.util.StringUtils;
import project.mockshop.policy.MockShopPolicy;

public class ItemValidator {
    public static void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("상품명"));
        }
    }

    public static void validatePrice(int price) {
        if (price <= 0) {
            throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("상품 가격"));
        }
    }

    public static void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("상품 재고"));
        }
    }
}
