package project.mockshop.entity;

import jakarta.persistence.*;
import lombok.*;
import project.mockshop.validator.ItemValidator;


@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"name", "price", "quantity"})
public class Item {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    private String thumbnail;
    private int price;
    private int quantity;
    private String descriptionImg1;
    private String descriptionImg2;
    private String descriptionImg3;
    private double percentOff;

    @ManyToOne(fetch = FetchType.LAZY)
    private Merchant merchant;


    public void changeName(String name) {
        ItemValidator.validateName(name);
        this.name = name;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void changePrice(int price) {
        ItemValidator.validatePrice(price);
        this.price = price;
    }

    public void changeQuantity(int quantity) {
        ItemValidator.validateQuantity(quantity);
        this.quantity = quantity;
    }

    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void changeDescriptionImg1(String descriptionImg1) {
        this.descriptionImg1 = descriptionImg1;
    }

    public void changeDescriptionImg2(String descriptionImg2) {
        this.descriptionImg2 = descriptionImg2;
    }

    public void changeDescriptionImg3(String descriptionImg3) {
        this.descriptionImg3 = descriptionImg3;
    }

    public void changePercentOff(double percentOff) {
        this.percentOff = percentOff;
    }

    public static class ItemBuilder {
        public ItemBuilder name(String name) {
            ItemValidator.validateName(name);
            this.name = name;
            return this;
        }

        public ItemBuilder price(int price) {
            ItemValidator.validatePrice(price);
            this.price = price;
            return this;
        }

        public ItemBuilder quantity(int quantity) {
            ItemValidator.validateQuantity(quantity);
            this.quantity = quantity;
            return this;
        }
    }
}