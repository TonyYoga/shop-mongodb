package com.telran.shopmongodb.service;

import com.telran.shopmongodb.controller.dto.*;
import com.telran.shopmongodb.data.CategoryRepository;
import com.telran.shopmongodb.data.OrderRepository;
import com.telran.shopmongodb.data.ProductRepository;
import com.telran.shopmongodb.data.UserRepository;
import com.telran.shopmongodb.data.entity.*;
import com.telran.shopmongodb.service.exceptions.NotFoundServiceException;
import com.telran.shopmongodb.service.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.telran.shopmongodb.service.Mapper.map;
import static java.util.stream.Collectors.toList;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    OrderRepository orderRepository;

    @Override
    public Optional<UserDto> addUserInfo(String email, String name, String phone) {
        UserEntity user = userRepository.findById(email).orElseThrow(() -> new NotFoundServiceException(String.format("Users %s not found", email)));
        UserProfile profile = new UserProfile(name,phone, BigDecimal.ZERO,null);
        user.setProfile(profile);
        userRepository.save(user);
        return Optional.of(map(profile));
    }

    @Override
    public Optional<UserDto> getUserInfo(String email) {
        UserEntity user = userRepository.findById(email).orElseThrow(() -> new NotFoundServiceException(String.format("Users %s not found", email)));
        return Optional.of(map(user.getProfile()));
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findBy()
                .map(Mapper::map)
                .collect(toList());
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findBy()
                .map(Mapper::map)
                .collect(toList());
    }

    @Override
    public List<ProductDto> getProductsByCategory(String categoryId) {
        return productRepository.findAllByCategory(categoryId)
                .map(Mapper::map)
                .collect(toList());
    }

    @Override
    public Optional<ShoppingCartDto> addProductToCart(String userEmail, String productId, int count) {
        UserEntity user = userRepository.findById(userEmail).orElseThrow(() -> new NotFoundServiceException(String.format("Users %s not found", userEmail)));
        ProductEntity product = productRepository.findById(productId).orElseThrow(() ->  new NotFoundServiceException(String.format("Product %s not found", userEmail, productId)));

        List<ProductOrder> sce = user.getProfile().getShoppingCart();
        if (sce == null) {
            sce = new ArrayList<>();
            user.getProfile().setShoppingCart(sce);
        }
        Optional<ProductOrder> po = sce.stream()
                .filter(productOrder -> productOrder.getProductId().equals(productId))
                .findAny();
        if (!po.isEmpty()) {
            po.get().setCount(po.get().getCount() + count);
            po.get().setPrice(product.getPrice());
        } else {
            sce.add(ProductOrder.builder()
                    .productId(productId)
                    .name(product.getName())
                    .price(product.getPrice())
                    .category(product.getCategory())
                    .count(count)
                    .build());
        }
        userRepository.save(user);
        return Optional.of(map(sce));
    }

    @Override
    public Optional<ShoppingCartDto> removeProductFromCart(String userEmail, String productId, int count) {
        UserEntity user = userRepository.findById(userEmail).orElseThrow(() -> new NotFoundServiceException(String.format("Users %s not found", userEmail)));
        List<ProductOrder> sce = user.getProfile().getShoppingCart();
        if (sce == null) {
            throw new ServiceException(String.format("Shopping cart %s is empty", userEmail));
        }
        ProductOrder poe = sce.stream()
                .filter(productOrder -> productOrder.getProductId().equals(productId))
                .findAny()
                .orElseThrow(() -> new NotFoundServiceException(String.format("Product with id %s not found", productId)));
        if (poe.getCount() <= count) {
            sce.remove(poe);
            return Optional.of(map(sce));
        }
        poe.setCount(poe.getCount() - count);
        userRepository.save(user);
        return Optional.of(map(sce));
    }

    @Override
    public Optional<ShoppingCartDto> getShoppingCart(String userEmail) {
        UserEntity user = userRepository.findById(userEmail).orElseThrow(() -> new NotFoundServiceException(String.format("Users %s not found", userEmail)));
        List<ProductOrder> sce = user.getProfile().getShoppingCart();
        if (sce == null) {
            throw new NotFoundServiceException(String.format("Shopping cart %s not found", userEmail));
        }
        return Optional.of(map(sce));
    }

    @Override
    public boolean clearShoppingCart(String userEmail) {
        UserEntity user = userRepository.findById(userEmail).orElseThrow(() -> new NotFoundServiceException(String.format("Users %s not found", userEmail)));
        List<ProductOrder> sce = user.getProfile().getShoppingCart();
        if (sce == null) {
            throw new NotFoundServiceException(String.format("Shopping cart %s not found", userEmail));
        }
        if (sce.isEmpty()) {
            return true;
        }
        sce.clear();
        userRepository.save(user);
        return true;
    }

    @Override
    public List<OrderDto> getOrders(String userEmail) {
        return orderRepository.findByOwnerEmail(userEmail).stream()
                .map(Mapper::map)
                .collect(toList());
    }

    @Override
    public Optional<OrderDto> checkout(String userEmail) {
        UserEntity user = userRepository.findById(userEmail).orElseThrow(() -> new NotFoundServiceException(String.format("Users %s not found", userEmail)));
        List<ProductOrder> sce = user.getProfile().getShoppingCart();
        if (user.getProfile() == null) {
            throw new NotFoundServiceException(String.format("Profile %s not full", userEmail));
        }
        var products = user.getProfile().getShoppingCart();
        BigDecimal totalCost = products.stream()
                .map(prod -> prod.getPrice().multiply(BigDecimal.valueOf(prod.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (user.getProfile().getBalance().compareTo(totalCost) < 0) {
            throw new ServiceException("Insufficient fonds");
        }
        user.getProfile().setBalance(user.getProfile().getBalance().subtract(totalCost));

        OrderEntity order = orderRepository.save(OrderEntity.builder()
                .date(Timestamp.valueOf(LocalDateTime.now()))
                .ownerEmail(user.getEmail())
                .status(OrderStatus.DONE)
                .products(products)
                .build());
        sce.clear();
        userRepository.save(user);
        return Optional.of(map(order));
    }
}
