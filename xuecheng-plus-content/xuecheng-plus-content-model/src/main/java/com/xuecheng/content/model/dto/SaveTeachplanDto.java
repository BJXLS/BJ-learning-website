package com.xuecheng.content.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author Hao Ge
 * @version 1.0
 * @description 保存课程计划dto，包括新增、修改
 * @date 2023/1/31 19:35
 */
@Data
@ToString
public class SaveTeachplanDto {

    private Long id;

    private String pname;

    private Long parentid;

    /**
     * 层级，分为1、2、3级
     */
    private Integer grade;

    /**
     * 课程类型:1视频、2文档
     */
    private String mediaType;

    /**
     * 课程标识
     */
    private Long courseId;

    /**
     * 课程发布标识
     */
    private Long coursePubId;

    /**
     * 是否支持试学或预览（试看）
     */
    private String isPreview;
}
