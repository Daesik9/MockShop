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
        if (findCustomer.getIsDeleted()) {
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
}
