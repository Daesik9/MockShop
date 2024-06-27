package project.mockshop.policy;

public class MockShopPolicy {
    private static final MockShopPolicy instance = new MockShopPolicy();

    private MockShopPolicy() {}

    public static MockShopPolicy getInstance() {
        return  instance;
    }

    /**
     * @param input
     * @return ${input}을/를 입력해주세요.
     */
    public static String INPUT_STRING_METHOD(String input) {
        return input + " 입력을 다시 확인해주세요.";
    }
}
