package com.qingchuan.coderman.admin.service;

import com.qingchuan.coderman.admin.vo.PageVO;
import com.qingchuan.coderman.admin.vo.QuestionVO;
import com.qingchuan.coderman.web.modal.Question;

/**
 * @Author zhangyukang
 * @Date 2020/3/12 11:06
 * @Version 1.0
 **/
public interface QuestionManagerService {

    /**
     * 帖子管理
     * @param QuestionVO
     * @return
     */
    PageVO<Question> list(QuestionVO QuestionVO);

    /**
     * 删除帖子
     * @param id
     */
    void delete(Integer id);

    /**
     * 置顶帖子
     * @param questionVO
     */
    void updateTop(QuestionVO questionVO);
}
