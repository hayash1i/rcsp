package ru.university.equiprent.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.university.equiprent.model.Category;
import ru.university.equiprent.model.Store;
import ru.university.equiprent.model.Supplier;
import ru.university.equiprent.model.User;
import ru.university.equiprent.repository.CategoryRepository;
import ru.university.equiprent.repository.StoreRepository;
import ru.university.equiprent.repository.SupplierRepository;
import ru.university.equiprent.repository.UserRepository;

@RestController
@RequestMapping("/api/refs")
public class ReferenceController {

    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public ReferenceController(CategoryRepository categoryRepository,
                               SupplierRepository supplierRepository,
                               StoreRepository storeRepository,
                               UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    // --- Categories ---
    @GetMapping("/categories")
    public List<Category> categories() { return categoryRepository.findAll(); }

    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) {
        category.setId(null);
        return categoryRepository.save(category);
    }

    // --- Suppliers ---
    @GetMapping("/suppliers")
    public List<Supplier> suppliers() { return supplierRepository.findAll(); }

    @PostMapping("/suppliers")
    public Supplier createSupplier(@RequestBody Supplier supplier) {
        supplier.setId(null);
        return supplierRepository.save(supplier);
    }

    // --- Stores ---
    @GetMapping("/stores")
    public List<Store> stores() { return storeRepository.findAll(); }

    @PostMapping("/stores")
    public Store createStore(@RequestBody Store store) {
        store.setId(null);
        return storeRepository.save(store);
    }

    // --- Users ---
    @GetMapping("/users")
    public List<User> users() { return userRepository.findAll(); }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        user.setId(null);
        return userRepository.save(user);
    }
}