package com.cn.lx.vo;
import com.cn.lx.goods.SimpleGoodsInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <h1>分页商品信息</h1>
 * */
@ApiModel(description = "分页商品信息对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageSimpleGoodsInfo {

    @ApiModelProperty(value = "分页简单商品信息")
    private List<SimpleGoodsInfo> simpleGoodsInfos;

    @ApiModelProperty(value = "是否有更多的商品(分页)")
    private Boolean hasMore;
}
