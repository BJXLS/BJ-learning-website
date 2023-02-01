package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import lombok.Data;

import java.util.List;

/**
 * @author Hao Ge
 * @version 1.0
 * @description 课程计划树形结构
 * @date 2023/1/31 18:48
 */
@Data
public class TeachplanDto extends Teachplan {

    // 课程计划关联的媒资信息
    TeachplanMedia teachplanMedia;

    // 子课程目录
    List<TeachplanDto> teachPlanTreeNodes;
}
