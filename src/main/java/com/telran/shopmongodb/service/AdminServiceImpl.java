package com.telran.shopmongodb.service;

import com.telran.shopmongodb.controller.dto.ProductStatisticDto;
import com.telran.shopmongodb.controller.dto.UserStatisticDto;
import com.telran.shopmongodb.data.CategoryRepository;
import com.telran.shopmongodb.data.OrderRepository;
import com.telran.shopmongodb.data.ProductRepository;
import com.telran.shopmongodb.data.UserRepository;
import com.telran.shopmongodb.data.entity.CategoryEntity;
import com.telran.shopmongodb.data.entity.ProductEntity;
import com.telran.shopmongodb.data.entity.UserEntity;
import com.telran.shopmongodb.service.exceptions.NotFoundServiceException;
import com.telran.shopmongodb.service.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;


    @Override
    public String addCategory(String categoryName) {
        if (!categoryRepository.existsById(categoryName)) {
            return categoryRepository.save(CategoryEntity.builder()
                    .name(categoryName)
                    .build()
            ).getName();
        }
        throw new ServiceException(String.format("Category %s already exist!", categoryName));
    }

    @Override
    public String addProduct(String productName, BigDecimal price, String categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundServiceException(String.format("No such category with id %s",categoryId)));
        return productRepository.save(ProductEntity.builder()
                .category(category)
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
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundServiceException(String.format("No such category with id %s",categoryId)));
        category.setName(categoryName);
        categoryRepository.save(category);
        //TODO fix category name at products
        return true;
    }

    @Override
    public boolean changeProductPrice(String productId, BigDecimal price) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundServiceException(String.format("Product %s not found", productId)));
        product.setPrice(price);
        productRepository.save(product);
        return true;
    }

    @Override
    public boolean addBalance(String userEmail, BigDecimal balance) {
        UserEntity user = userRepository.findById(userEmail)
                .orElseThrow(() -> new NotFoundServiceException(String.format("Users %s not found", userEmail)));
        user.getProfile().setBalance(balance);
        userRepository.save(user);
        return true;
    }

    @Override
    public List<ProductStatisticDto> getMostPopularProduct() {
        //TODO
        return null; //productOrderRepository.getPopularProductStatistics().map(Mapper::map).collect(toList());
    }

    @Override
    public List<ProductStatisticDto> getMostProfitableProduct() {
        //TODO
        return null; //productOrderRepository.getProfitableProductStatistics().map(Mapper::map).collect(toList());
    }

    @Override
    public List<UserStatisticDto> getMostActiveUser() {
        //TODO
//        List<UserStatisticDto> resStat = orderRepository.getMostActiveUser().map(Mapper::map).collect(toList());
//        resStat.forEach(stat -> stat.setProducts(
//                productOrderRepository.findByOrder_Owner_Email(stat.getUserEmail())
//                        .map(Mapper::map)
//                        .collect(Collectors.toList())));
//        return resStat;
        return null;
    }

    @Override
    public List<UserStatisticDto> getMostProfitableUser() {
        //TODO
//        List<UserStatisticDto> resStat = orderRepository.getMostProfitableUser().map(Mapper::map).collect(toList());
//        resStat.forEach(stat -> stat.setProducts(
//                productOrderRepository.findByOrder_Owner_Email(stat.getUserEmail())
//                        .map(Mapper::map)
//                        .collect(Collectors.toList())));
        return null;
    }
}
