package com.gzu.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzu.stock.entity.ProductStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductStockMapper extends BaseMapper<ProductStock> {

    @Update("update t_product_stock set stock = stock - #{quantity} " +
            "where product_code = #{productCode} and stock >= #{quantity}")
    int deduct(@Param("productCode") String productCode, @Param("quantity") int quantity);
}
