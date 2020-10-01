package com.qingchuan.coderman.web.dao;

import com.qingchuan.coderman.web.modal.QuestionZan;
import com.qingchuan.coderman.web.modal.QuestionZanExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionZanMapper {
    int countByExample(QuestionZanExample example);

    int deleteByExample(QuestionZanExample example);

    int deleteByPrimaryKey(Long id);

    int insert(QuestionZan record);

    int insertSelective(QuestionZan record);

    List<QuestionZan> selectByExample(QuestionZanExample example);

    QuestionZan selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") QuestionZan record, @Param("example") QuestionZanExample example);

    int updateByExample(@Param("record") QuestionZan record, @Param("example") QuestionZanExample example);

    int updateByPrimaryKeySelective(QuestionZan record);

    int updateByPrimaryKey(QuestionZan record);
}