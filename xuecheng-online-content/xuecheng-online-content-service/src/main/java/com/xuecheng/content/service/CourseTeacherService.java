package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

/**
 * 课程教师管理业务接口
 */
public interface CourseTeacherService {

    /**
     * 查询课程教师信息列表
     * @param courseId 课程id
     * @return
     */
    public List<CourseTeacher> getCourseTeacherList(Long courseId);

    /**
     * 添加或修改教师
     * @param courseTeacher 教师信息
     * @return
     */
    public CourseTeacher addOrUpdateCourseTeacher(CourseTeacher courseTeacher);

    /**
     * 删除教师
     * @param courseId 课程id
     * @param teacherId 教师id(主键)
     */
    void deleteCourseTeacher(Long courseId, Long teacherId);
}
