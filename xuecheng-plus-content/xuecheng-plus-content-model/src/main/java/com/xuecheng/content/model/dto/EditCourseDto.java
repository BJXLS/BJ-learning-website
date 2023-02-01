package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Hao Ge
 * @version 1.0
 * @description 修改课程DTO
 * @date 2023/1/29 21:38
 */
@Data
@ApiModel(value = "EditCourseDto", description = "修改课程基本信息")
public class EditCourseDto extends AddCourseDto{

    @ApiModelProperty(value = "课程名称", required = true)
    private Long id;
}
