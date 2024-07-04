package project.mockshop.entity;

import org.junit.jupiter.api.Test;
import project.mockshop.policy.MockShopPolicy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 상품명, 카테고리, 상품 이미지, 가격, 총 재고, 상세 설명 이미지, 할인율을 첨부하면 상품을 등록할 수 있다.
 * 상품명, 가격, 총 재고는 필수로 입력해야한다.
 * 가격은 0보다 같거나 작을 수 없다.
 * 재고는 0보다 같거나 작을 수 없다.
 * 상품을 등록하면 판매자 PK가 자동으로 설정된다.
 * 상품 번호는 자동으로 부여한다.
 */
public class ItemTest {
    @Test
    void itemNotNull() throws Exception {
        //given
        Item item;

        //when
        item = Item.builder().build();

        //then
        assertThat(item).isNotNull();
    }

    @Test
    void createItem() throws Exception {
        //given
        Item item = Item.builder()
                .id(1L)
                .name("name")
                .category(new Category("Book"))
                .thumbnail("image.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(5)
//                .merchantId(1L)
                .build();

        //when

        //then
        assertThat(item.getName()).isEqualTo("name");
    }

    @Test
    void createItem_fail_noName() throws Exception {
        //given

        //when
        assertThatThrownBy(() -> Item.builder()
                .name("")
                .price(1000)
                .quantity(100)
                .build()).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("상품명"));

        //then
    }

    @Test
    void createItem_fail_zeroPriceOrLess() throws Exception {
        //given

        //when
        assertThatThrownBy(() -> Item.builder()
                .name("상품명")
                .price(0)
                .quantity(100)
                .build()).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("상품 가격"));

        assertThatThrownBy(() -> Item.builder()
                .name("상품명")
                .price(-100)
                .quantity(100)
                .build()).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("상품 가격"));

        //then
    }

    @Test
    void createItem_fail_zeroQuantityOrLess() throws Exception {
        //given

        //when
        assertThatThrownBy(() -> Item.builder()
                .name("상품명")
                .price(100)
                .quantity(0)
                .build()).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("상품 재고"));

        assertThatThrownBy(() -> Item.builder()
                .name("상품명")
                .price(100)
                .quantity(-100)
                .build()).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MockShopPolicy.INPUT_STRING_METHOD("상품 재고"));

        //then
    }
}
