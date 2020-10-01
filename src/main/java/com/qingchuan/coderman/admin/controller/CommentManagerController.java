package com.qingchuan.coderman.admin.controller;

import com.qingchuan.coderman.admin.service.CommentManagerService;
import com.qingchuan.coderman.admin.vo.CommentVO;
import com.qingchuan.coderman.admin.vo.PageVO;
import com.qingchuan.coderman.web.dto.ResultTypeDTO;
import com.qingchuan.coderman.web.modal.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 评论管理
 **/
@Controller
@RequestMapping("/admin/commentManager")
@SuppressWarnings("all")
public class CommentManagerController {

    @Autowired
    private CommentManagerService commentManagerService;
    
    /**
     * 跳转到评论管理页面
     */
    @GetMapping("/comments")
    public String comments(){
        return "admin/manager/comments";
    }


    /**
     * 评论列表
     */
    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public PageVO<Comment> list(CommentVO commentVO){
        PageVO<Comment> page=commentManagerService.list(commentVO);
        return page;
    }

    /**
     * 删除评论
     */
    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public ResultTypeDTO delete(@RequestParam("id") Integer id){
        try {
            commentManagerService.delete(id);
            return new ResultTypeDTO().okOf();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultTypeDTO().errorOf("删除评论失败");
        }
    }

}
