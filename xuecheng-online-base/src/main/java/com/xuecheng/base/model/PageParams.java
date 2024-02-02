package com.xuecheng.base.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  分页查询通用参数
 */
@Data
public class PageParams {
    //当前页码
    @ApiModelProperty("当前页码")
    private Long pageNo=1L;
    //每页记录数
    @ApiModelProperty("每页记录数")
    private Long pageSize=10L;

    public PageParams(){

    }

    public PageParams(long pageNo,long pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

}
