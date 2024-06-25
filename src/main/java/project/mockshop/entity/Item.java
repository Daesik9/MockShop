package project.mockshop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import project.mockshop.validator.ItemValidator;

@Builder
@Getter
@Entity
public class Item {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String category;
    private String thumbnail;
    private int price;
    private int quantity;
    private String descriptionImg1;
    private String descriptionImg2;
    private String descriptionImg3;
    private double percentOff;
    private Long merchantId;

    public void changeName(String name) {
        ItemValidator.validateName(name);
        this.name = name;
    }

    public void changeCategory(String category) {
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