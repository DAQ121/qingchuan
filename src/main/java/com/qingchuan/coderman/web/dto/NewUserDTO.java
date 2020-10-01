package com.qingchuan.coderman.web.dto;

import com.qingchuan.coderman.web.modal.User;

public class NewUserDTO extends User {
    //问题数
    private Integer questionCount;
    //点赞人数
    private Integer likeCount;
    //粉丝数
    private Integer fansCount;
    //问题发出时间
    private Integer time;

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}
