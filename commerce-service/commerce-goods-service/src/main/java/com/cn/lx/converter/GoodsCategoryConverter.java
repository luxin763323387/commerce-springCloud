package com.cn.lx.converter;

import com.cn.lx.enums.GoodsCategory;

import javax.persistence.AttributeConverter;

/**
 * <h1>商品类别枚举属性转换器</h1>
 */
public class GoodsCategoryConverter implements AttributeConverter<GoodsCategory, String> {

    @Override
    public String convertToDatabaseColumn(GoodsCategory goodsCategory) {
        return goodsCategory.getCode();
    }

    @Override
    public GoodsCategory convertToEntityAttribute(String code) {
        return GoodsCategory.of(code);
    }
}
