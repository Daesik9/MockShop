package project.mockshop.repository;

import project.mockshop.entity.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomerRepository {

    private final Map<Long, Customer> store = new HashMap<>();
    private Long sequence = 0L;

    public void save(Customer customer) {
        customer.changeId(++sequence);
        store.put(customer.getId(), customer);
    }

    public Optional<Customer> findById(Long id) {
        Customer findCustomer = store.get(id);
        if (findCustomer == null || findCustomer.getIsDeleted()) {
            return Optional.empty();
        }
        return Optional.of(findCustomer);
    }

    public List<Customer> findAll() {
        return store.values().stream().filter(c -> !c.getIsDeleted()).toList();
    }

    public void delete(Long id) {
        Optional<Customer> findCustomer = findById(id);
        findCustomer.ifPresent(customer -> customer.changeIsDeleted(true));
    }

    public Optional<Customer> findByLoginId(String loginId) {
        return store.values().stream().filter(c -> c.getLoginId().equals(loginId))
                .findAny();
    }

    public Optional<Customer> findLoginIdByPhoneNumber(String phoneNumber) {
        return store.values().stream().filter(c -> c.getPhoneNumber().equals(phoneNumber))
                .findAny();
    }
}
