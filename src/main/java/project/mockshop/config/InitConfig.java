package project.mockshop.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import project.mockshop.dto.ItemDto;
import project.mockshop.entity.Customer;
import project.mockshop.mapper.CustomerMapper;
import project.mockshop.service.CustomerService;
import project.mockshop.service.ItemService;

@Component
@RequiredArgsConstructor
@Profile("local")
public class InitConfig {
    private final ItemService itemService;
    private final CustomerService customerService;

    @PostConstruct
    public void init() {
        itemService.createItem(ItemDto.builder().name("사과").price(1000).quantity(100).build(), 1L);
        Customer customer = Customer.builder().name("테스트").password("Password1!").email("test@gmail.com").loginId("test").phoneNumber("01011111111").build();
        customerService.createAccount(CustomerMapper.toCreationDto(customer));
    }

}
