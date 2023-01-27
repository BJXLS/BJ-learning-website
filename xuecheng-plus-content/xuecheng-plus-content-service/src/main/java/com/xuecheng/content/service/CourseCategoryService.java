package com.xuecheng.content.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;

import java.util.List;

/**
 * <p>
 * 课程分类 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-01-27
 */
public interface CourseCategoryService extends IService<CourseCategory> {
    /**
     * @description 课程分类查询
     * @param id
     * @return java.util.List<com.xuecheng.content.model.dto.CourseCategoryTreeDto>
     * @author Hao Ge
     * @date 2023/1/27 12:07*/
    public List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
