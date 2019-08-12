package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.ProductOrder;
import com.telran.shopmongodb.data.entity.ShoppingCart;
import com.telran.shopmongodb.data.projection.ProductStatistics;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Stream;

public interface ProductOrderRepository extends CrudRepository<ProductOrder, String> {
    ProductOrder findProductOrderEntityByProductIdAndShoppingCart(String productId, ShoppingCart shoppingCart);
    void deleteByShoppingCart(ShoppingCart shoppingCart);
    List<ProductOrder> findProductOrderEntitiesByShoppingCart(ShoppingCart shoppingCart);
    List<ProductOrder> findProductOrderEntitiesByCategory_Id(String categoryName);
    Stream<ProductOrder> findByOrder_Owner(String email);
    Stream<ProductOrder> findByOrder_Owner_Email(String email);
    default boolean isCategoryNotUsed(String categoryId) {
        var prods = findProductOrderEntitiesByCategory_Id(categoryId);
        if (!findProductOrderEntitiesByCategory_Id(categoryId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Category %s is used in data", categoryId));
        }
        return true;
    }

    Stream<ProductStatistics> getPopularProductStatistics();

    Stream<ProductStatistics> getProfitableProductStatistics();
}
