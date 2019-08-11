package com.telran.shopmongodb.data.projection;

import java.math.BigDecimal;

public interface ProductStatistics {
    String getProductName();
    String getProductCategory();
    long getNumberOfPurchases();
    BigDecimal getTotalAmount();
}
