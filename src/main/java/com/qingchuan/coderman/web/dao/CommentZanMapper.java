package com.qingchuan.coderman.web.dao;

import com.qingchuan.coderman.web.modal.CommentZan;
import com.qingchuan.coderman.web.modal.CommentZanExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CommentZanMapper {
    int countByExample(CommentZanExample example);

    int deleteByExample(CommentZanExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CommentZan record);

    int insertSelective(CommentZan record);

    List<CommentZan> selectByExample(CommentZanExample example);

    CommentZan selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CommentZan record, @Param("example") CommentZanExample example);

    int updateByExample(@Param("record") CommentZan record, @Param("example") CommentZanExample example);

    int updateByPrimaryKeySelective(CommentZan record);

    int updateByPrimaryKey(CommentZan record);
}