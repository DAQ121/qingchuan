package com.qingchuan.coderman.web.service;

import com.github.pagehelper.PageInfo;
import com.qingchuan.coderman.web.dto.*;
import com.qingchuan.coderman.web.dto.*;
import com.qingchuan.coderman.web.modal.Question;
import com.qingchuan.coderman.web.modal.User;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("all")
@Service
public interface QuestionService {

    void doPublish(Question question);

    PageInfo<Question> getPage(Integer pageNo, Integer pageSize);

    PageInfo<Question> findQuestionsByUserId(Integer pageNo, Integer pageSize, Integer id);

    Question findQuestionById(Integer id);

    void updateQuestion(Question question);

    ResultTypeDTO saveOrUpdate(Question question);

    List<Question> relatedQuestions(Question question);

    void incViewCount(Question question);

    List<CommentDTO> findQuestionComments(Integer id);

    PageInfo<Question> getPageBySearch(QuestionQueryDTO questionQueryDTO);


    List<QuestionDTO> findNewQuestion(int i);

    PageInfo<Question> findQuestionsByCategory(Integer pageNo, Integer pageSize, Integer categoryVal);

    List<QuestionDTO> findRecommendQuestions(int pageno, int pagesize);

    PageInfo<Question> getCollectPage(Integer pageNo, Integer pageSize, Integer userId);

    List<User> findCollectUsers(Integer id);

    PageInfo<Question> findQuestionsWithUserByTopic(TopicQueryDTO topicQueryDTO);
}
