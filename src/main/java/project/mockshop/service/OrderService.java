package project.mockshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.mockshop.dto.OrderDto;
import project.mockshop.entity.Customer;
import project.mockshop.entity.Order;
import project.mockshop.mapper.OrderMapper;
import project.mockshop.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

//    public List<OrderDto> findAllByMerchant(Merchant merchant) {
//        List<Order> orders = orderRepository.findAllByMerchant(merchant);
//
//        return orders.stream().map(OrderMapper::toDto).toList();
//    }

    public List<OrderDto> findAllByCustomer(Customer customer) {
        List<Order> orders = orderRepository.findAllByCustomer(customer);

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
        return OrderMapper.toDto(orderRepository.findByOrderNumber(orderNumber));
    }

    public List<OrderDto> findAllByOrderDate(LocalDateTime from, LocalDateTime to) {
        return orderRepository.findAllByOrderDateBetween(from, to).stream().map(OrderMapper::toDto).toList();
    }

    public List<OrderDto> findAllByStatus(String status) {
        return orderRepository.findAllByStatus(status).stream().map(OrderMapper::toDto).toList();
    }

    public List<OrderDto> findAll() {
        return orderRepository.findAll().stream().map(OrderMapper::toDto).toList();
    }
}