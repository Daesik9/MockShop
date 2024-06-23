package project.mockshop.validator;

import org.springframework.util.StringUtils;
import project.mockshop.policy.CustomerPolicy;
import project.mockshop.policy.MockShopPolicy;

public class CustomerValidator {
    public static void validateLoginId(String loginId) {
        if (!StringUtils.hasText(loginId)) {
            throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("로그인 id"));
        }

        loginIdLengthCheck(loginId);

        if (StringUtils.containsWhitespace(loginId)) {
            throw new IllegalArgumentException(CustomerPolicy.LOGIN_ID_POLICY_STRING);
        }

        for (int i = 0; i < loginId.length(); i++) {
            char c = loginId.charAt(i);
            if (!(Character.isLowerCase(c) || Character.isDigit(c) || c == '-' || c == '_')) {
                throw new IllegalArgumentException(CustomerPolicy.LOGIN_ID_POLICY_STRING);
            }
        }
    }

    private static void loginIdLengthCheck(String loginId) {
        if (loginId.length() < CustomerPolicy.MIN_LOGIN_ID_LENGTH
                || loginId.length() > CustomerPolicy.MAX_LOGIN_ID_LENGTH) {
            throw new IllegalArgumentException(CustomerPolicy.LOGIN_ID_LENGTH_STRING);
        }
    }

    public static void validatePassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("비밀번호"));
        }
        if (password.length() < CustomerPolicy.MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(CustomerPolicy.MIN_PASSWORD_STRING);
        }
        if (password.length() > CustomerPolicy.MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(CustomerPolicy.MAX_PASSWORD_STRING);
        }

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException(CustomerPolicy.PASSWORD_POLICY_STRING);
        }
    }

    public static boolean isValidPassword(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }

            if (hasUppercase && hasLowercase && hasDigit && hasSpecialChar) {
                return true;
            }
        }
        return false;
    }

    public static void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("이름"));
        }
        if (name.length() < CustomerPolicy.MIN_NAME_LENGTH || name.length() > CustomerPolicy.MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(CustomerPolicy.NAME_LENGTH_STRING);
        }
        if (!name.matches(CustomerPolicy.NAME_POLICY_REGEX)) {
            throw new IllegalArgumentException(CustomerPolicy.NAME_POLICY_STRING);
        }
    }

    public static void validatePhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            throw new IllegalArgumentException(MockShopPolicy.INPUT_STRING_METHOD("전화번호"));
        }

        for (char c : phoneNumber.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("전화번호 xxx-xxxx-xxxx 포맷에 맞게 -를 제외한 숫자로만 구성되어야 합니다.");
            }
        }

        if (phoneNumber.length() != 11) {
            throw new IllegalArgumentException("전화번호 xxx-xxxx-xxxx 포맷에 맞게 -를 제외한 숫자로만 구성되어야 합니다.");
        }
    }

    public static void validatePoint(int point) {
        if (point < 0) {
            throw new IllegalArgumentException("포인트는 음수일 수 없습니다.");
        }
    }

    public static void validateEmail(String email) {
        final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!email.matches(EMAIL_PATTERN)) {
            throw new IllegalArgumentException("이메일 형식을 다시 확인해주세요.");
        }
    }
}
