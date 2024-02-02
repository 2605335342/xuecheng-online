package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachPlanDto;
import com.xuecheng.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程计划编辑接口
 */
@RestController
@Api(tags = "课程计划编辑接口")
public class TeachPlanController {

    @Autowired
    private TeachplanService teachplanService;

    @ApiOperation("查询课程计划")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachPlanDto> queryTreeNodes(@PathVariable Long courseId){
        return teachplanService.queryTreeNodes(courseId);
    }

    @ApiOperation("新增或修改课程计划")
    @PostMapping("/teachplan")
    public void saveOrUpdateTeachplan(@RequestBody SaveTeachplanDto saveTeachplanDto){
        teachplanService.saveOrUpdateTeachplan(saveTeachplanDto);
    }

    @ApiOperation("删除课程计划")
    @DeleteMapping("/teachplan/{id}")
    public void deleteTeachplan(@PathVariable Long id){
        teachplanService.deleteTeachplanById(id);
    }

    @ApiOperation("课程计划排序")
    @PostMapping("/teachplan/{moveType}/{id}")
    public void orderByMove(@PathVariable String moveType,@PathVariable Long id){
        teachplanService.orderByMove(moveType,id);
    }
}
