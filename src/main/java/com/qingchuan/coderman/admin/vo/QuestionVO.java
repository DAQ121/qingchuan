package com.qingchuan.coderman.admin.vo;

import com.qingchuan.coderman.web.modal.Question;
import lombok.Data;

@Data
@SuppressWarnings("all")
public class QuestionVO extends Question {
    private int page;
    private int limit;
}
