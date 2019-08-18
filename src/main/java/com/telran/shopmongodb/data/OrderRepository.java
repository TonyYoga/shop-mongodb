package com.telran.shopmongodb.data;

import com.telran.shopmongodb.data.entity.OrderEntity;
import com.telran.shopmongodb.data.projection.ProductStatistic;
import com.telran.shopmongodb.data.projection.UserStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.schema.JsonSchemaObject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

public interface OrderRepository extends MongoRepository<OrderEntity, String>, CustomOrderRepository {
    List<OrderEntity> findByOwnerEmail(String userEmail);


}

interface CustomOrderRepository {
    Stream<UserStatistic> getMostActiveUser();
    Stream<UserStatistic> getMostProfitableUser();
    Stream<ProductStatistic> getPopularProductStatistics();
    Stream<ProductStatistic> getProfitableProductStatistics();

}

@Repository
class CustomOrderRepositoryImpl implements CustomOrderRepository {
    @Autowired
    MongoTemplate template;

    /* Native NoSQL query
    * [{
    $unwind: {
        path: "$products"
    }
}, {
    $group: {
        _id: "$ownerEmail",
        totalCount: {
            $sum: "$products.count"
        },
        totalAmount: {
            $sum: {
                $multiply: ["$products.count", {
                    $toDouble: "$products.price"
                }]
            }
        },
        prodList: {
            $push: "$products"
        }
    }
}]*/
    @Override
    public Stream<UserStatistic> getMostActiveUser() {
        UnwindOperation unwind = Aggregation.unwind("products");
        ProjectionOperation project = Aggregation.project("ownerEmail")
                .and("products").as("product")
                .and("products.count").as("count")
                .and("products.count")
                .multiply(ConvertOperators.Convert.convertValueOf("products.price").to(JsonSchemaObject.Type.doubleType())).as("price");
        GroupOperation group = Aggregation.group("ownerEmail")
                .last("ownerEmail").as("userEmail")
                .sum("count").as("totalProductsCount")
                .sum("price").as("totalAmount")
                .push("product").as("productList");
        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "totalProductsCount");
        List<UserStatistic> stat = template.aggregate(Aggregation.newAggregation(unwind, project, group, sort), OrderEntity.class, UserStatistic.class).getMappedResults();
        return stat.stream();
    }

    @Override
    public Stream<UserStatistic> getMostProfitableUser() {
        UnwindOperation unwind = Aggregation.unwind("products");
        ProjectionOperation project = Aggregation.project("ownerEmail")
                .and("products").as("product")
                .and("products.count").as("count")
                .and("products.count")
                .multiply(ConvertOperators.Convert.convertValueOf("products.price")
                        .to(JsonSchemaObject.Type.doubleType()))
                .as("price");
        GroupOperation group = Aggregation.group("ownerEmail")
                .last("ownerEmail").as("userEmail")
                .sum("count").as("totalProductsCount")
                .sum("price").as("totalAmount")
                .push("product").as("productList");
        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "totalAmount");
        List<UserStatistic> stat = template.aggregate(Aggregation.newAggregation(unwind, project, group, sort), OrderEntity.class, UserStatistic.class).getMappedResults();
        return stat.stream();
    }
/*Native NoSQL
*
* [{
    $unwind: {
        path: "$products"
    }
}, {
    $project: {
        _id: "$products.productId",
        prodName: "$products.name",
        prodCount: "$products.count",
        prodAmount: {
            $multiply: ["$products.count", {
                $toDouble: "$products.price"
            }]
        },
        prodCat: "$products.category"

    }
}, {
    $group: {
        _id: "$prodName",
        totalCount: {
            $sum: "$prodCount"
        },
        totalAmount: {
            $sum: "$prodAmount"
        },
        prodCat: {
            $mergeObjects: {
                $mergeObjects: "$products.category"
            }
        }
    }
}]*/

    @Override
    public Stream<ProductStatistic> getPopularProductStatistics() {
        UnwindOperation unwind = Aggregation.unwind("products");
        ProjectionOperation proj = Aggregation.project("products.productId")
                .and("products.name").as("productName")
                .and("products.count").as("count")
                .and("products.category").as("category")
                .and("products.count")
                .multiply(ConvertOperators.Convert.convertValueOf("products.price")
                        .to(JsonSchemaObject.Type.doubleType()))
                .as("price");

        GroupOperation group = Aggregation.group("productName")
                .last("productName").as("productName")
                .last("category").as("productCategory")
                .sum("count").as("numberOfPurchases")
                .sum("price").as("totalAmount");
        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "numberOfPurchases");
        List<ProductStatistic> stat = template.aggregate(Aggregation.newAggregation(unwind,proj,group,sort), OrderEntity.class, ProductStatistic.class).getMappedResults();
        return stat.stream();
    }

    @Override
    public Stream<ProductStatistic> getProfitableProductStatistics() {
        UnwindOperation unwind = Aggregation.unwind("products");
        ProjectionOperation proj = Aggregation.project("products.productId")
                .and("products.name").as("productName")
                .and("products.count").as("count")
                .and("products.category").as("category")
                .and("products.count")
                .multiply(ConvertOperators.Convert.convertValueOf("products.price")
                        .to(JsonSchemaObject.Type.doubleType()))
                .as("price");

        GroupOperation group = Aggregation.group("productName")
                .last("productName").as("productName")
                .last("category").as("productCategory")
                .sum("count").as("numberOfPurchases")
                .sum("price").as("totalAmount");
        SortOperation sort = Aggregation.sort(Sort.Direction.DESC, "totalAmount");
        List<ProductStatistic> stat = template.aggregate(Aggregation.newAggregation(unwind,proj,group,sort), OrderEntity.class, ProductStatistic.class).getMappedResults();
        return stat.stream();
    }
}