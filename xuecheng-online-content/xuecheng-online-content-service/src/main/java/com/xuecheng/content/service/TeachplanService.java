package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachPlanDto;

import java.util.List;

/**
 * 课程计划管理业务接口
 */
public interface TeachplanService {

    /**
     * 课程计划详细信息树型结构查询
     * @param courseId 课程id
     * @return
     */
    public List<TeachPlanDto> queryTreeNodes(Long courseId);

    /**
     * 新增或修改课程计划
     * @param saveTeachplanDto 保存课程计划dto
     */
    public void saveOrUpdateTeachplan(SaveTeachplanDto saveTeachplanDto);

    /**
     * 根据主键删除课程计划
     * @param id 课程计划id
     */
    public void deleteTeachplanById(Long id);

    /**
     * 课程计划排序（上移下移）
     * @param moveType 移动类型
     * @param id 课程计划id
     */
    void orderByMove(String moveType, Long id);
}
