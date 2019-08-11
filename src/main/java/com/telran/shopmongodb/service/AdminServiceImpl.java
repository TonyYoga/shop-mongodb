package com.telran.shopmongodb.service;

import com.telran.shopmongodb.controller.dto.ProductStatisticDto;
import com.telran.shopmongodb.controller.dto.UserStatisticDto;
import com.telran.shopmongodb.data.*;
import com.telran.shopmongodb.data.entity.CategoryEntity;
import com.telran.shopmongodb.data.entity.ProductEntity;
import com.telran.shopmongodb.service.exceptions.NotFoundServiceException;
import com.telran.shopmongodb.service.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class AdminServiceImpl implements AdminService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductOrderRepository productOrderRepository;
    @Autowired
    OrderRepository orderRepository;


    @Override
    public String addCategory(String categoryName) {
        if (!categoryRepository.existsById(categoryName)) {
            return categoryRepository.save(CategoryEntity.builder()
                    .name(categoryName)
                    .build()
            ).getId();
        }
        throw new ServiceException(String.format("Category %s already exist!", categoryName));
    }

    @Override
    public String addProduct(String productName, BigDecimal price, String categoryId) {
        Optional<CategoryEntity> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new NotFoundServiceException(String.format("No such category with id %s",categoryId));
        }
        return productRepository.save(ProductEntity.builder()
                .category(category.get())
                .name(productName)
                .price(price)
                .build()
        ).getId();
    }

    @Override
    public boolean removeProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundServiceException(String.format("No such product with id %s:",productId));
        }
        productRepository.deleteById(productId);
        return true;
    }

    /**
     * Method removeCategory not allowed!
     *
     * @throws ServiceException
     */
    @Override
    public boolean removeCategory(String categoryId) {
/*        productOrderRepository.isCategoryNotUsed(categoryId);
        if (!categoryRepository.existsById(categoryId)) {
            return false;
        }
        categoryRepository.deleteById(categoryId);
        return true;*/
        throw new ServiceException("Method removeCategory not allowed!");
    }

    @Override
    public boolean updateCategory(String categoryId, String categoryName) {
        Optional<CategoryEntity> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new RuntimeException(String.format("Category with id %s not found!", categoryId));
        }
        category.get().setName(categoryName);
        return true;
    }

    @Override
    public boolean changeProductPrice(String productId, BigDecimal price) {
        Optional<ProductEntity> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new NotFoundServiceException(String.format("Product %s not found", productId));
        }
        product.get().setPrice(price);
        return true;
    }

    @Override
    public boolean addBalance(String userEmail, BigDecimal balance) {
        var user = userRepository.findById(userEmail);
        if (user.isEmpty()) {
            throw new NotFoundServiceException(String.format("User %s not found", userEmail));
        }
        user.get().setBalance(balance);
        return true;
    }

    @Override
    public List<ProductStatisticDto> getMostPopularProduct() {
        return productOrderRepository.getPopularProductStatistics().map(Mapper::map).collect(toList());
    }

    @Override
    public List<ProductStatisticDto> getMostProfitableProduct() {
        return productOrderRepository.getProfitableProductStatistics().map(Mapper::map).collect(toList());
    }

    @Override
    public List<UserStatisticDto> getMostActiveUser() {
        List<UserStatisticDto> resStat = orderRepository.getMostActiveUser().map(Mapper::map).collect(toList());
        resStat.forEach(stat -> stat.setProducts(
                productOrderRepository.findByOrder_Owner_Email(stat.getUserEmail())
                        .map(Mapper::map)
                        .collect(Collectors.toList())));
        return resStat;
    }

    @Override
    public List<UserStatisticDto> getMostProfitableUser() {
        List<UserStatisticDto> resStat = orderRepository.getMostProfitableUser().map(Mapper::map).collect(toList());
        resStat.forEach(stat -> stat.setProducts(
                productOrderRepository.findByOrder_Owner_Email(stat.getUserEmail())
                        .map(Mapper::map)
                        .collect(Collectors.toList())));
        return resStat;
    }
}
