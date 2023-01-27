package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import org.springframework.stereotype.Service;

/**
 * @author Hao Ge
 * @version 1.0
 * @description 课程基本信息管理业务接口
 * @date 2023/1/26 19:00
 */
public interface CourseBaseInfoService {

    /**
     * @description 课程查询接口
     * @param pageParams 分页参数
     * @param queryCourseParamsDto 查询条件
     * @return com.xuecheng.base.model.PageResult<com.xuecheng.content.model.po.CourseBase>
     * @author Hao Ge
     * @date 2023/1/26 19:03
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);

    /***
     * @description 添加课程基本信息
     * @param companyId
     * @param addCourseDto
     * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
     * @author Hao Ge
     * @date 2023/1/27 12:57*/
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);
}
