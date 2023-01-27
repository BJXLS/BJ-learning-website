package com.xuecheng.content;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Hao Ge
 * @version 1.0
 * @description TODO
 * @date 2023/1/27 12:24
 */
@SpringBootTest
public class CourseCategoryServiceTests {
    @Autowired
    CourseCategoryService courseCategoryService;

    @Test
    void testQueryTreeNodes() {
        List<CourseCategoryTreeDto> categoryTreeDtos = courseCategoryService.queryTreeNodes("1");
        System.out.println(categoryTreeDtos);
    }
}
