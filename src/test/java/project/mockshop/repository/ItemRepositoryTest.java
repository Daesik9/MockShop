package project.mockshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.mockshop.entity.Item;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Test
    void repositoryNotNull() throws Exception {
        //given

        //when
        assertThat(itemRepository).isNotNull();

        //then
    }

    @Test
    void create() throws Exception {
        //given
        Item item = Item.builder().name("item").price(1000).quantity(100).build();

        //when
        Item savedItem = itemRepository.save(item);

        //then
        assertThat(savedItem.getId()).isEqualTo(item.getId());
    }

    @Test
    void findAll() throws Exception {
        //given
        Item item1 = Item.builder().name("item1").price(1000).quantity(100).build();
        Item item2 = Item.builder().name("item2").price(2000).quantity(200).build();
        Item item3 = Item.builder().name("item3").price(3000).quantity(300).build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        //when
        List<Item> items = itemRepository.findAll();

        //then
        assertThat(items.size()).isEqualTo(3);
        assertThat(items.get(1)).isEqualTo(item2);
    }

    @Test
    void findById() throws Exception {
        //given
        Item item = Item.builder().name("item").price(1000).quantity(100).build();
        itemRepository.save(item);

        //when
        Optional<Item> findItem = itemRepository.findById(item.getId());

        //then
        assertThat(findItem).isNotEmpty();
        assertThat(findItem.get().getName()).isEqualTo("item");
    }

    @Test
    void delete() throws Exception {
        //given
        Item item1 = Item.builder().name("item1").price(1000).quantity(100).build();
        Item item2 = Item.builder().name("item2").price(2000).quantity(200).build();
        Item item3 = Item.builder().name("item3").price(3000).quantity(300).build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        //when
        itemRepository.delete(item2);

        //then
        List<Item> items = itemRepository.findAll();
        assertThat(items.size()).isEqualTo(2);
        assertThat(items.get(1)).isEqualTo(item3);
    }

    @Test
    void update() throws Exception {
        //given
        Item item1 = Item.builder().name("item1").price(1000).quantity(100).build();
        Item item2 = Item.builder().name("item2").price(2000).quantity(200).build();
        Item item3 = Item.builder().name("item3").price(3000).quantity(300).build();
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        Optional<Item> findItemOptional = itemRepository.findById(item2.getId());
        Item findItem = findItemOptional.orElse(null);

        //when
        assert findItem != null;
        findItem.changeName("newName");
        findItem.changeCategory("newCategory");
        findItem.changePrice(10000);
        findItem.changeQuantity(10000);
        findItem.changeThumbnail("new-thumbnail.png");
        findItem.changeDescriptionImg1("new-image1.png");
        findItem.changeDescriptionImg2("new-image2.png");
        findItem.changeDescriptionImg3("new-image3.png");
        findItem.changePercentOff(0.7);

        //then
        Optional<Item> changedItemOptional = itemRepository.findById(findItem.getId());
        assertThat(changedItemOptional).isNotEmpty();
        Item changedItem = changedItemOptional.get();
        assertThat(changedItem.getName()).isEqualTo("newName");
        assertThat(changedItem.getCategory()).isEqualTo("newCategory");
        assertThat(changedItem.getPrice()).isEqualTo(10000);
        assertThat(changedItem.getQuantity()).isEqualTo(10000);
        assertThat(changedItem.getThumbnail()).isEqualTo("new-thumbnail.png");
        assertThat(changedItem.getDescriptionImg1()).isEqualTo("new-image1.png");
        assertThat(changedItem.getDescriptionImg2()).isEqualTo("new-image2.png");
        assertThat(changedItem.getDescriptionImg3()).isEqualTo("new-image3.png");
        assertThat(changedItem.getPercentOff()).isEqualTo(0.7);
    }
}