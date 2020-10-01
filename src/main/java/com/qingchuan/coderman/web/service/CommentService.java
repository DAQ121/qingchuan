package com.qingchuan.coderman.web.service;

import com.qingchuan.coderman.web.dto.CommentDTO;
import com.qingchuan.coderman.web.modal.Comment;

import java.util.List;

public interface CommentService {

    void doComment(Comment comment);

    List<CommentDTO> findSecondComments(Integer id);
}
