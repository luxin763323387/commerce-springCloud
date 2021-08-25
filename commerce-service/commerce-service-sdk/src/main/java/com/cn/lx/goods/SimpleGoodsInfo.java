package com.cn.lx.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author StevenLu
 * @date 2021/8/25 下午11:38
 */
@ApiModel(description = "商品信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleGoodsInfo implements Serializable {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品图片")
    private String goodsPic;

    @ApiModelProperty(value = "商品金额")
    private Integer price;
}
