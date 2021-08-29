package com.cn.lx.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 扣减商品库存
 *
 * @author StevenLu
 * @date 2021/8/28 下午7:58
 */
@ApiModel(description = "扣减商品库存")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeductGoodsInventory implements Serializable {

    @ApiModelProperty(value = "扣减库存id")
    private Long goodsId;

    @ApiModelProperty(value = "扣减库存数量")
    private Integer count;

}
