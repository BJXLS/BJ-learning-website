package com.xuecheng.base.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Hao Ge
 * @version 1.0
 * @description 分页参数对象
 * @date 2023/1/23 8:53
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageParams {

    // 当前页码默认值
    public static final long DEFAULT_PAGE_CURRENT = 1L;

    // 当前记录数默认值
    public static final long DEFAULT_PAGE_SIZE = 10L;

    // 当前页码
    @ApiModelProperty("当前页码")
    private Long pageNo = DEFAULT_PAGE_CURRENT;

    // 每页记录数默认值
    @ApiModelProperty("每页记录数默认值")
    private Long pageSize = DEFAULT_PAGE_SIZE;
}
