package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;

import java.util.List;

/**
 * @author Hao Ge
 * @version 1.0
 * @description 课程计划管理业务接口
 * @date 2023/1/31 19:17
 */
public interface TeachplanService {

    /***
     * @description 查询课程计划树形结构
     * @param courseId
     * @return java.util.List<com.xuecheng.content.model.dto.TeachplanDto>
     * @author Hao Ge
     * @date 2023/1/31 19:18*/
    public List<TeachplanDto> findTeachplanTree(long courseId);

    /***
     * @description 添加课程计划
     * @param teachplanDto
     * @return void
     * @author Hao Ge
     * @date 2023/1/31 19:42*/
    public void saveTeachplan(SaveTeachplanDto teachplanDto);
}
