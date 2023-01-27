package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Hao Ge
 * @version 1.0
 * @description 课程分类树形节点dto
 * @date 2023/1/27 11:51
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {
    List childrenTreeNodes;
}
