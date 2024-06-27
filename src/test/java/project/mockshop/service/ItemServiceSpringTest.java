package project.mockshop.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.mockshop.dto.ItemDto;
import project.mockshop.entity.Category;
import project.mockshop.entity.Item;
import project.mockshop.entity.Merchant;
import project.mockshop.repository.CategoryRepository;
import project.mockshop.repository.ItemRepository;
import project.mockshop.repository.MerchantRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ItemServiceSpringTest {
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MerchantRepository merchantRepository;

    Category category;
    @BeforeEach
    void beforeEach() {
        category = new Category("category");
        categoryRepository.save(category);
    }

    @Test
    void createItem() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        merchantRepository.save(merchant);

        ItemDto itemDto = ItemDto.builder()
                .name("name")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .merchant(merchant)
                .percentOff(10)
                .build();

        //when
        Long itemId = itemService.createItem(itemDto, merchant.getId());


        //then
        Item findItem = itemRepository.findById(itemId).orElse(null);
        assertThat(findItem.getName()).isEqualTo("name");
    }

    @Test
    void findItemsByName() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        ItemDto itemDto1 = ItemDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .name("asdfname")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto3 = ItemDto.builder()
                .name("nameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto4 = ItemDto.builder()
                .name("name")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto5 = ItemDto.builder()
                .name("asdfasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1, merchant.getId());
        itemService.createItem(itemDto2, merchant.getId());
        itemService.createItem(itemDto3, merchant.getId());
        itemService.createItem(itemDto4, merchant.getId());
        itemService.createItem(itemDto5, merchant.getId());

        //when
        List<ItemDto> findItems = itemService.findItemsByNameLike("name");

        for (ItemDto findItem : findItems) {
            System.out.println("findItem.getName() = " + findItem.getName());
        }

        //then
        assertThat(findItems.size()).isEqualTo(4);
    }

    @Test
    void findItemsByMerchantName() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        merchantRepository.save(merchant);

        ItemDto itemDto1 = ItemDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .name("asdfname")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemDto itemDto3 = ItemDto.builder()
                .name("nameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto4 = ItemDto.builder()
                .name("name")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto5 = ItemDto.builder()
                .name("asdfasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1, merchant.getId());
        itemService.createItem(itemDto2, merchant.getId());
        itemService.createItem(itemDto3, merchant.getId());
        itemService.createItem(itemDto4, merchant.getId());
        itemService.createItem(itemDto5, merchant.getId());

        //when
        List<ItemDto> findItems = itemService.findItemsByMerchantName("merchant");

        //then
        assertThat(findItems.size()).isEqualTo(2);
    }

    @Test
    void findItemsByCategory() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        Category category1 = new Category("category1");
        categoryRepository.save(category1);
        Long merchantId = 1L;
        ItemDto itemDto1 = ItemDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .name("asdfname")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto3 = ItemDto.builder()
                .name("nameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto4 = ItemDto.builder()
                .name("name")
                .category(category1)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto5 = ItemDto.builder()
                .name("asdfasdf")
                .category(category1)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1, merchantId);
        itemService.createItem(itemDto2, merchantId);
        itemService.createItem(itemDto3, merchantId);
        itemService.createItem(itemDto4, merchantId);
        itemService.createItem(itemDto5, merchantId);

        //when
        List<ItemDto> findItems = itemService.findAllByCategory(category);

        //then
        assertThat(findItems.size()).isEqualTo(3);
    }

    @Test
    void findItemsByQuantity() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        merchantRepository.save(merchant);

        ItemDto itemDto1 = ItemDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .name("asdfname")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(200)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemDto itemDto3 = ItemDto.builder()
                .name("nameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(300)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemDto itemDto4 = ItemDto.builder()
                .name("name")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(400)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();
        ItemDto itemDto5 = ItemDto.builder()
                .name("asdfasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(500)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .merchant(merchant)
                .build();

        itemService.createItem(itemDto1, merchant.getId());
        itemService.createItem(itemDto2, merchant.getId());
        itemService.createItem(itemDto3, merchant.getId());
        itemService.createItem(itemDto4, merchant.getId());
        itemService.createItem(itemDto5, merchant.getId());

        //when
        List<ItemDto> findItems = itemService.findItemsByQuantity(0, 300);

        //then
        assertThat(findItems.size()).isEqualTo(3);
        assertThatThrownBy(() -> itemService.findItemsByQuantity(-100, 300))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> itemService.findItemsByQuantity(100, -300))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> itemService.findItemsByQuantity(-100, -300))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> itemService.findItemsByQuantity(300, 100))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findItemsByPrice() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        ItemDto itemDto1 = ItemDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .name("asdfname")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(2000)
                .quantity(200)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto3 = ItemDto.builder()
                .name("nameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(3000)
                .quantity(300)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto4 = ItemDto.builder()
                .name("name")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(4000)
                .quantity(400)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto5 = ItemDto.builder()
                .name("asdfasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(5000)
                .quantity(500)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1, merchant.getId());
        itemService.createItem(itemDto2, merchant.getId());
        itemService.createItem(itemDto3, merchant.getId());
        itemService.createItem(itemDto4, merchant.getId());
        itemService.createItem(itemDto5, merchant.getId());

        //when
        List<ItemDto> findItems = itemService.findItemsByPrice(2000, 4000);

        //then
        assertThat(findItems.size()).isEqualTo(3);
        assertThatThrownBy(() -> itemService.findItemsByPrice(-2000, 4000))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> itemService.findItemsByPrice(2000, -4000))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> itemService.findItemsByPrice(-2000, -4000))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> itemService.findItemsByPrice(4000, 2000))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findDiscountItems() throws Exception {
        //given
        Merchant merchant = Merchant.builder().name("merchant").build();
        ItemDto itemDto1 = ItemDto.builder()
                .name("asdfnameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(1000)
                .quantity(100)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto2 = ItemDto.builder()
                .name("asdfname")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(2000)
                .quantity(200)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .build();
        ItemDto itemDto3 = ItemDto.builder()
                .name("nameasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(3000)
                .quantity(300)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .build();
        ItemDto itemDto4 = ItemDto.builder()
                .name("name")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(4000)
                .quantity(400)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();
        ItemDto itemDto5 = ItemDto.builder()
                .name("asdfasdf")
                .category(category)
                .thumbnail("thumbnail.png")
                .price(5000)
                .quantity(500)
                .descriptionImg1("img1.png")
                .descriptionImg2("img2.png")
                .descriptionImg3("img3.png")
                .percentOff(10)
                .build();

        itemService.createItem(itemDto1, merchant.getId());
        itemService.createItem(itemDto2, merchant.getId());
        itemService.createItem(itemDto3, merchant.getId());
        itemService.createItem(itemDto4, merchant.getId());
        itemService.createItem(itemDto5, merchant.getId());

        //when
        List<ItemDto> findItems = itemService.findDiscountItems();

        //then
        assertThat(findItems.size()).isEqualTo(3);
    }
}