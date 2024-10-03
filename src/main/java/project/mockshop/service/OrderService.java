package project.mockshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.mockshop.dto.OrderDto;
import project.mockshop.entity.*;
import project.mockshop.mapper.OrderMapper;
import project.mockshop.repository.CartRepository;
import project.mockshop.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

//    public List<OrderDto> findAllByMerchant(Merchant merchant) {
//        List<Order> orders = orderRepository.findAllByMerchant(merchant);
//
//        return orders.stream().map(OrderMapper::toDto).toList();
//    }

    public List<OrderDto> findAllByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findAllByCustomerId(customerId);

        return orders.stream().map(OrderMapper::toDto).toList();
    }

    public List<OrderDto> findAllByCustomerName(String customerName) {
        List<Order> orders = orderRepository.findAllByCustomerName(customerName);

        return orders.stream().map(OrderMapper::toDto).toList();
    }

    public List<OrderDto> findAllByCustomerLoginId(String customerLoginId) {
        List<Order> orders = orderRepository.findAllByCustomerLoginId(customerLoginId);

        return orders.stream().map(OrderMapper::toDto).toList();
    }

    public List<OrderDto> findAllByCustomerPhoneNumber(String customerPhoneNumber) {
        List<Order> orders = orderRepository.findAllByCustomerPhoneNumber(customerPhoneNumber);

        for (Order order : orders) {
            System.out.println("order = " + order.getId());
        }

        return orders.stream().map(OrderMapper::toDto).toList();
    }

//    public List<OrderDto> findAllByMerchantName(String merchantName) {
//        List<Order> orders = orderRepository.findAllByMerchantName(merchantName);
//
//        return orders.stream().map(OrderMapper::toDto).toList();
//    }

    public List<OrderDto> findAllByItemName(String itemName) {
        List<Order> orders = orderRepository.findAllByItemName(itemName);

        return orders.stream().map(OrderMapper::toDto).toList();
    }

    public OrderDto findByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new NullPointerException("해당 주문 번호와 일치하는 주문이 없습니다."));
        return OrderMapper.toDto(order);
    }

    public List<OrderDto> findAllByOrderDate(LocalDateTime from, LocalDateTime to) {
        return orderRepository.findAllByOrderDateBetween(from, to).stream().map(OrderMapper::toDto).toList();
    }

    public List<OrderDto> findAllByStatus(OrderStatus status) {
        return orderRepository.findAllByStatus(status).stream().map(OrderMapper::toDto).toList();
    }

    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream().map(OrderMapper::toDto).toList();
    }

    @Transactional
    public String order(Long customerId, String paymentMethod) {
        //장바구니에서 상품 가져오기
        Cart cart = cartRepository.findCartWithItems(customerId);

        //장바구니에 든 상품들로 OrderItem 만들기
        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(ci -> OrderItem.createOrderItem(ci.getItem(), ci.getCartPrice(), ci.getCount()))
                .toList();

        //Order 만들기
        Order newOrder = Order.createOrder(cart.getCustomer(), paymentMethod, orderItems);

        cart.removeAllCartItems();
        orderRepository.save(newOrder);

        return newOrder.getOrderNumber();
    }

    public List<OrderDto> findAllByMerchantId(Long merchantId) {
        List<Order> allByMerchantId = orderRepository.findAllByMerchantId(merchantId);

        return allByMerchantId.stream().map(OrderMapper::toDto).toList();
    }
}