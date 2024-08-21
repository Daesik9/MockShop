package project.mockshop.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import project.mockshop.entity.Category;
import project.mockshop.entity.Merchant;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"name", "price", "quantity"})
public class ItemCreationDto {
    private Long id;
    private String name;
    private Category category;
    private MultipartFile thumbnail;
    private int price;
    private int quantity;
    private MultipartFile descriptionImg1;
    private MultipartFile descriptionImg2;
    private MultipartFile descriptionImg3;
    private double percentOff;
    private Merchant merchant;
}
