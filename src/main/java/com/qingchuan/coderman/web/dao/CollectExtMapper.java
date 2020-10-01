package com.qingchuan.coderman.web.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CollectExtMapper {
    @Select("select question_Id from collect where user_Id=#{userId}")
    List<Integer> findQuestionById(Integer userId);
}
