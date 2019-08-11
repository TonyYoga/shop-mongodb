package com.telran.shopmongodb.data.projection;

import java.math.BigDecimal;

public interface UserStatistics {
    String getUserEmail();
    long getTotalProductsCount();
    BigDecimal getTotalAmount();
}
