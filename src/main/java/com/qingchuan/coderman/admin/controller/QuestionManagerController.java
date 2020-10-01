package com.qingchuan.coderman.admin.controller;

import com.qingchuan.coderman.admin.service.QuestionManagerService;
import com.qingchuan.coderman.admin.vo.PageVO;
import com.qingchuan.coderman.admin.vo.QuestionVO;
import com.qingchuan.coderman.web.dto.ResultTypeDTO;
import com.qingchuan.coderman.web.modal.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 帖子管理
 **/
@Controller
@RequestMapping("/admin/questionManager")
@SuppressWarnings("all")
public class QuestionManagerController {

    @Autowired
    private QuestionManagerService questionManagerService;

    /**
     * 跳转到用户管理页面
     */
    @RequestMapping(value = "/questions",method = RequestMethod.GET)
    public String users(){
        return "admin/manager/questions";
    }

    /**
     * 用户列表
     */
    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public PageVO<Question> list(QuestionVO questionVO){
        PageVO<Question> page=questionManagerService.list(questionVO);
        return page;
    }

    /**
     * 删除用户
     */
    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public ResultTypeDTO delete(@RequestParam("id") Integer id){
        try {
            questionManagerService.delete(id);
            return new ResultTypeDTO().okOf();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultTypeDTO().errorOf("删除帖子失败");
        }
    }

    /**
     * 置顶帖子
     */
    @ResponseBody
    @RequestMapping(value = "/updateTop",method = RequestMethod.GET)
    public ResultTypeDTO updateTop(QuestionVO questionVO){
        try {
            questionManagerService.updateTop(questionVO);
            return new ResultTypeDTO().okOf();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultTypeDTO().errorOf("帖子置顶失败");
        }
    }

}
