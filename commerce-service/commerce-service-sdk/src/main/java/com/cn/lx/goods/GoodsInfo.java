package com.cn.lx.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author StevenLu
 * @date 2021/8/25 下午11:13
 */
@ApiModel(description = "商品信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfo implements Serializable {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "商品类别")
    private String goodsCategory;

    @ApiModelProperty(value = "品牌类别")
    private String brandCategory;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品图片")
    private String goodsPic;

    @ApiModelProperty(value = "商品描述")
    private String goodsDescription;

    @ApiModelProperty(value = "商品状态")
    private Integer goodsStatus;

    @ApiModelProperty(value = "商品价格")
    private Integer price;

    @ApiModelProperty(value = "商品属性")
    private GoodsProperty goodsProperty;

    @ApiModelProperty(value = "商品供应量")
    private Long supply;

    @ApiModelProperty(value = "商品库存")
    private Long inventory;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModel(description = "商品属性")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GoodsProperty {

        @ApiModelProperty(value = "尺寸")
        private String size;

        @ApiModelProperty(value = "颜色")
        private String color;

        @ApiModelProperty(value = "材质")
        private String material;

        @ApiModelProperty(value = "图案")
        private String pattern;
    }
}
