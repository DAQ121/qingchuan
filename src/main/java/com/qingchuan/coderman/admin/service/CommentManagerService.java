package com.qingchuan.coderman.admin.service;

import com.qingchuan.coderman.admin.vo.CommentVO;
import com.qingchuan.coderman.admin.vo.PageVO;
import com.qingchuan.coderman.web.modal.Comment;
@SuppressWarnings("all")
public interface CommentManagerService {

    /**
     * 评论管理
     */
    PageVO<Comment> list(CommentVO commentVO);

    /**
     * 删除评论
     */
    void delete(Integer id);
    
}
