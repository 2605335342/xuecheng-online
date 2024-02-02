package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XuechengException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachPlanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 课程计划管理业务接口实现类
 */
@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    /**
     * 课程计划详细信息树型结构查询
     * @param courseId 课程id
     * @return
     */
    @Override
    public List<TeachPlanDto> queryTreeNodes(Long courseId) {
        List<TeachPlanDto> teachPlanDtos = teachplanMapper.selectTreeNodes(courseId);
        return teachPlanDtos;
    }


    /**
     * 新增或修改课程计划
     * @param saveTeachplanDto 保存课程计划dto
     */
    @Transactional
    @Override
    public void saveOrUpdateTeachplan(SaveTeachplanDto saveTeachplanDto) {
        //获取课程计划id
        Long id = saveTeachplanDto.getId();
        if(id==null){
            //新增课程计划
            int count = getTeachplanCount(saveTeachplanDto.getCourseId(), saveTeachplanDto.getParentid());
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(saveTeachplanDto,teachplan);
            teachplan.setOrderby(count+1);  //设置排序号
            teachplanMapper.insert(teachplan);
        }else{
            //修改课程计划
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(saveTeachplanDto,teachplan);
            teachplanMapper.updateById(teachplan);
        }
    }

    /**
     * 获取最新的排序号
     * @param courseId 课程id
     * @param parentid 课程计划父级id
     * @return
     */
    private int getTeachplanCount(Long courseId,Long parentid){
        LambdaQueryWrapper<Teachplan> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Teachplan::getCourseId,courseId);
        lambdaQueryWrapper.eq(Teachplan::getParentid,parentid);
        //查出多条数据选择1条，按降序排序选择orderby最大的1条
        lambdaQueryWrapper.orderByDesc(Teachplan::getOrderby);
        lambdaQueryWrapper.last("limit 1");  //selectOne查出多条数据会报tooManyResult错误，这里限制只返回1条

        //select * from teachplan where course_id = ? and parentid = ? order by orderby Desc limit 1
        Teachplan teachplan = teachplanMapper.selectOne(lambdaQueryWrapper);
        Integer orderby = teachplan.getOrderby();
        return orderby;
    }


    /**
     * 根据主键删除课程计划
     * @param id 课程计划id
     */
    @Transactional
    @Override
    public void deleteTeachplanById(Long id) {
        //参数校验
        if(id==null){
            throw new XuechengException("课程计划id为空");
        }

        Teachplan teachplan = teachplanMapper.selectById(id);
        Integer grade = teachplan.getGrade();

        if(grade==1){
            //删除大章节，需判断大章节下是否有小章节，若没有则正常删除，若有则无法删除。
            LambdaQueryWrapper<Teachplan> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Teachplan::getCourseId,teachplan.getCourseId());
            lambdaQueryWrapper.eq(Teachplan::getParentid,teachplan.getId());
            Integer count = teachplanMapper.selectCount(lambdaQueryWrapper);

            if(count==0){
                teachplanMapper.deleteById(id);
            }else{
                throw new XuechengException("该大章节下有小章节，无法删除");
            }
        }

        if(grade==2){
            //删除小章节，需要将其关联的media也删除
            LambdaQueryWrapper<TeachplanMedia> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(TeachplanMedia::getTeachplanId,id);
            lambdaQueryWrapper.eq(TeachplanMedia::getCourseId,teachplan.getCourseId());

            teachplanMapper.deleteById(id);
            teachplanMediaMapper.delete(lambdaQueryWrapper);
        }
    }


    /**
     * 课程计划排序（上移下移）
     * @param moveType 移动类型
     * @param id 课程计划id
     */
    @Override
    public void orderByMove(String moveType, Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        Integer orderby = teachplan.getOrderby();

        if("moveup".equals(moveType)){
            //上移
            //判断是否为边界值
            if(orderby==1){
                throw new XuechengException("无法上移");
            }

            //找出同级别的上一个teachplan（注意：上个teachplan的orderby！=orderby-1）
            LambdaQueryWrapper<Teachplan> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Teachplan::getCourseId,teachplan.getCourseId());
            lambdaQueryWrapper.eq(Teachplan::getParentid,teachplan.getParentid());
            lambdaQueryWrapper.lt(Teachplan::getOrderby,orderby);
            lambdaQueryWrapper.orderByDesc(Teachplan::getOrderby);
            lambdaQueryWrapper.last("limit 1");
            Teachplan teachplan1 = teachplanMapper.selectOne(lambdaQueryWrapper);

            //交换orderby值
            teachplan.setOrderby(teachplan1.getOrderby());
            teachplan1.setOrderby(orderby);
            //修改操作
            teachplanMapper.updateById(teachplan);
            teachplanMapper.updateById(teachplan1);
        }else if("movedown".equals(moveType)){
            //movedown下移
            int count = getTeachplanCount(teachplan.getCourseId(), teachplan.getParentid());
            //判断是否为边界值
            if(orderby==count){
                throw new XuechengException("无法下移");
            }

            //找出同级别的下一个teachplan（注意：上个teachplan的orderby！=orderby+1）
            LambdaQueryWrapper<Teachplan> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Teachplan::getCourseId,teachplan.getCourseId());
            lambdaQueryWrapper.eq(Teachplan::getParentid,teachplan.getParentid());
            lambdaQueryWrapper.gt(Teachplan::getOrderby,orderby);
            lambdaQueryWrapper.orderByAsc(Teachplan::getOrderby);
            lambdaQueryWrapper.last("limit 1");

            //select * from teachplan where course_id = ? and parentid=? and orderby>? order by orderby asc limit 1
            Teachplan teachplan2 = teachplanMapper.selectOne(lambdaQueryWrapper);

            //交换orderby值
            teachplan.setOrderby(teachplan2.getOrderby());
            teachplan2.setOrderby(orderby);
            //修改操作
            teachplanMapper.updateById(teachplan);
            teachplanMapper.updateById(teachplan2);
        }
    }


}
