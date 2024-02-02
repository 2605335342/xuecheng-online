package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程教师管理业务接口实现类
 */
@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    /**
     * 查询课程教师信息列表
     * @param courseId 课程id
     * @return
     */
    @Override
    public List<CourseTeacher> getCourseTeacherList(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CourseTeacher::getCourseId,courseId);

        //select * from course_teacher where course_id = ?
        List<CourseTeacher> courseTeacherList = courseTeacherMapper.selectList(lambdaQueryWrapper);
        return courseTeacherList;
    }


    /**
     * 添加或修改教师
     * @param courseTeacher 教师信息
     * @return
     */
    @Transactional
    @Override
    public CourseTeacher addOrUpdateCourseTeacher(CourseTeacher courseTeacher) {
        Long id = courseTeacher.getId();
        if(id==null){
            //添加教师
            courseTeacher.setCreateDate(LocalDateTime.now());
            courseTeacherMapper.insert(courseTeacher);
            return courseTeacher;
        }else{
            //修改教师
            courseTeacherMapper.updateById(courseTeacher);
            //根据id查询教师信息并返回
            CourseTeacher dbCourseTeacher = courseTeacherMapper.selectById(id);
            return dbCourseTeacher;
        }
    }


    /**
     * 删除教师
     * @param courseId 课程id
     * @param teacherId 教师id(主键)
     */
    @Override
    public void deleteCourseTeacher(Long courseId, Long teacherId) {
        LambdaQueryWrapper<CourseTeacher> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CourseTeacher::getCourseId,courseId);
        lambdaQueryWrapper.eq(CourseTeacher::getId,teacherId);
        courseTeacherMapper.delete(lambdaQueryWrapper);
    }


}
