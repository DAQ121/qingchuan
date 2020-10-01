package com.qingchuan.coderman.admin.controller;

import com.qingchuan.coderman.admin.service.UserManagerService;
import com.qingchuan.coderman.admin.vo.PageVO;
import com.qingchuan.coderman.admin.vo.UserVO;
import com.qingchuan.coderman.web.dto.ResultTypeDTO;
import com.qingchuan.coderman.web.modal.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台用户管理接口
 **/
@Controller
@RequestMapping("/admin/userManager")
@SuppressWarnings("all")
public class UserManagerController {

    @Autowired
    private UserManagerService userManagerService;

    /**
     * 跳转到用户管理页面
     */
    @RequestMapping(value = "/users",method = RequestMethod.GET)
    public String users(){
        return "admin/manager/users";
    }

    /**
     * 用户列表
     */
    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public PageVO<User> list(UserVO userVO){
        PageVO<User> page=userManagerService.list(userVO);
        return page;
    }

    /**
     * 删除用户
     */
    @ResponseBody
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public ResultTypeDTO delete(@RequestParam("id") Integer id){
        try {
            userManagerService.delete(id);
            return new ResultTypeDTO().okOf();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultTypeDTO().errorOf("删除用户失败");
        }
    }

    /**
     * 更新等级
     */
    @ResponseBody
    @RequestMapping(value = "/setRank",method = RequestMethod.POST)
    public ResultTypeDTO setRank(UserVO userVO){
        try {
            userManagerService.setRank(userVO);
            return new ResultTypeDTO().okOf();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultTypeDTO().errorOf("等级更新失败");
        }
    }
}
