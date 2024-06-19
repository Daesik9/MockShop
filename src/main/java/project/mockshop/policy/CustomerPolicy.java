package project.mockshop.policy;

public class CustomerPolicy {
    private static final CustomerPolicy instance = new CustomerPolicy();

    private CustomerPolicy() {}

    public static CustomerPolicy getInstance() {
        return instance;
    }

    public static final int MIN_LOGIN_ID_LENGTH = 3;
    public static final int MAX_LOGIN_ID_LENGTH = 15;

    public static final String LOGIN_ID_LENGTH_STRING = "로그인 아이디는 " + CustomerPolicy.MIN_LOGIN_ID_LENGTH + "-" +
            CustomerPolicy.MAX_LOGIN_ID_LENGTH + "자리로 구성되어야 합니다.";

    public static final String LOGIN_ID_POLICY_STRING = "로그인 아이디는 소문자와 숫자, -, _로만 구성되어야 합니다.";

    public static final int MIN_PASSWORD_LENGTH = 8;

    public static final String MIN_PASSWORD_STRING = "비밀번호는 최소 " + MIN_PASSWORD_LENGTH + "자 이상이어야 합니다.";

    public static final int MAX_PASSWORD_LENGTH = 128;
    public static final String MAX_PASSWORD_STRING = "비밀번호는 최대 " + MAX_PASSWORD_LENGTH + "128자 이하여야 합니다.";

    public static final String PASSWORD_POLICY_STRING = "비밀번호는 대소문자, 특수문자, 숫자가 포함되어야 합니다.";

    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 15;
    public static final String NAME_LENGTH_STRING =
            "이름은 " + MIN_NAME_LENGTH + "-" + MAX_NAME_LENGTH + "자로 구성되어야 합니다.";

    public static final String NAME_POLICY_REGEX = "[가-힣]*";
    public static final String NAME_POLICY_STRING = "이름은 한글로만 구성되어야 합니다.";

    public static final String PHONENUMBER_POLICY_STRING = "전화번호 xxx-xxxx-xxxx 포맷에 맞게 -를 제외한 숫자로만 구성되어야 합니다.";

    public static final String EMAIL_POLICY_STRING = "이메일 형식을 다시 확인해주세요.";

    public static final String ZIPCODE_POLICY_STRING = "우편번호는 숫자로만 구성되어야 합니다.";

    public static final String POINT_POLICY_STRING = "포인트는 음수일 수 없습니다.";

    public static final String DUPLICATE_LOGIN_ID_STRING = "이미 중복된 로그인 아이디가 있습니다.";
}