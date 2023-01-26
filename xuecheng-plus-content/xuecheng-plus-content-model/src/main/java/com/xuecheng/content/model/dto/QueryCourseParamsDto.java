package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author Hao Ge
 * @version 1.0
 * @description 课程查询参数Dto
 * @date 2023/1/23 9:00
 */
@Data
@ToString
public class QueryCourseParamsDto {

    // 审核状态
    @ApiModelProperty("审核状态")
    private String auditStatus;

    // 课程名称
    @ApiModelProperty("课程名称")
    private String courseName;

    // 发布状态
    private String publishStatus;
}
