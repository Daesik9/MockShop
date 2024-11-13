package project.mockshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.advice.ExceptionAdvice;
import project.mockshop.annotation.WithMockMember;
import project.mockshop.dto.*;
import project.mockshop.entity.Category;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Member;
import project.mockshop.entity.Merchant;
import project.mockshop.mapper.CustomerMapper;
import project.mockshop.repository.CategoryRepository;
import project.mockshop.repository.MerchantRepository;
import project.mockshop.service.CartService;
import project.mockshop.service.CustomerService;
import project.mockshop.service.ItemService;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class CartControllerSpringTest {
    @Autowired
    private CartController cartController;
    @Autowired
    private CartService cartService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MerchantRepository merchantRepository;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    Long itemId;
    Long customerId;

    @BeforeEach
    void init() {
//        mockMvc = MockMvcBuilders.standaloneSetup(cartController)
//                .setControllerAdvice(new ExceptionAdvice())
//                .build();
        Category category = new Category("category");
        categoryRepository.save(category);
        Merchant merchant = Merchant.builder().name("merchant").storeName("merchant_store").build();
        merchantRepository.save(merchant);

        ItemCreationDto creationDto = ItemCreationDto.builder()
                .name("사과")
                .price(1000)
                .quantity(100)
                .merchant(merchant)
                .category(category)
                .build();
        try {
            itemId = itemService.createItem(creationDto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Customer customer = Customer.builder().name("테스트").password("Password1!").email("test@gmail.com").loginId("test").phoneNumber("01011111111").build();
        customerId = customerService.createAccount(CustomerMapper.toCreationDto(customer));
    }

    @Test
    void mockMvcNotNull() {
        assertThat(cartController).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @WithMockMember
    void addToCart() throws Exception {
        //given
        CartAddRequestDto cartRequest = CartAddRequestDto.builder().itemId(itemId).count(3).customerId(customerId).build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequest))
        );

        //then
        resultActions.andExpect(jsonPath("$.code").value(200));
    }

    @Test
    @WithMockMember //멤버
//    @WithMockMember(id = customerId) -> constant value밖에 못 넣음.
    void getCartWithItems() throws Exception {
        //given
        cartService.addToCart(itemId, 3, customerId);

        //customerId 1로 로그인했는데도  2인 유저의 정보를 가져올 수 있음 -> 그러면 안됨!!!
        //컨트롤러에서 현재 로그인한 계정의 id로 조회하도록 로직을 수정함.
        //그래서 테스트시에 user 정보를 넣어줘야해서 이렇게 수정함.
        Member member = Customer.builder().id(customerId).build();
        CustomUserDetails userDetails = new CustomUserDetails(member);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/cart")
                        .with(user(userDetails))

//                        .param("customerId", customerId.toString())
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result.data.cartItemDtos[0].count").value(3))
                .andExpect(jsonPath("$.result.data.cartItemDtos[0].cartPrice").value(3000));
    }

    @Test
    @WithMockMember
    void changeCartItemCount() throws Exception {
        //given
        Long cartId = cartService.addToCart(itemId, 3, customerId);
        CartDto cartDtoWithItems = cartService.getCartWithItems(customerId);
        List<CartItemDto> cartItems = cartDtoWithItems.getCartItemDtos();

        CartChangeRequestDto changeRequestDto = CartChangeRequestDto.builder()
                .cartId(cartId)
                .cartItemId(cartItems.get(0).getId())
                .count(4)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeRequestDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
        assertThat(cartService.getCartWithItems(customerId).getCartItemDtos().get(0).getCount()).isEqualTo(4);
        assertThat(cartService.getCartWithItems(customerId).getCartItemDtos().get(0).getCartPrice()).isEqualTo(4000);
    }

    @Test
    @WithMockMember
    void changeCartItemCount_fail() throws Exception {
        //given
        Long cartId = cartService.addToCart(itemId, 3, customerId);
        CartDto cartWithItems = cartService.getCartWithItems(customerId);
        List<CartItemDto> cartItems = cartWithItems.getCartItemDtos();

        CartChangeRequestDto changeRequestDto = CartChangeRequestDto.builder()
                .cartId(cartId)
                .cartItemId(cartItems.get(0).getId())
                .count(101)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeRequestDto))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.result.msg").value("재고가 부족합니다."));
        assertThat(cartService.getCartWithItems(customerId).getCartItemDtos().get(0).getCount()).isEqualTo(3);
        assertThat(cartService.getCartWithItems(customerId).getCartItemDtos().get(0).getCartPrice()).isEqualTo(3000);
    }

    @Test
    @WithMockMember
    void removeCartItem() throws Exception {
        //given
        Long cartId = cartService.addToCart(itemId, 3, customerId);
        CartDto cartWithItems = cartService.getCartWithItems(customerId);
        List<CartItemDto> cartItems = cartWithItems.getCartItemDtos();
        CartDeleteRequestDto deleteRequestDto = CartDeleteRequestDto.builder()
                .cartId(cartId)
                .cartItemId(cartItems.get(0).getId())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteRequestDto))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
        assertThat(cartService.getCartWithItems(customerId).getCartItemDtos().size()).isEqualTo(0);
    }

    @Test
    @WithMockMember
    void removeCartItem_fail() throws Exception {
        //given
        Long cartId = cartService.addToCart(itemId, 3, customerId);
        CartDeleteRequestDto deleteRequestDto = CartDeleteRequestDto.builder()
                .cartId(cartId)
                .cartItemId(1L)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteRequestDto))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.result.msg").value("장바구니에 해당 상품이 없습니다."));
    }


}