package com.qingchuan.coderman.admin.vo;

import com.qingchuan.coderman.web.modal.Comment;
import lombok.Data;

@Data
@SuppressWarnings("all")
public class CommentVO extends Comment {
    private int page;
    private int limit;
}
