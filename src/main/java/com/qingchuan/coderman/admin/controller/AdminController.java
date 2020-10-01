package com.qingchuan.coderman.admin.controller;

import com.qingchuan.coderman.web.dto.ResultTypeDTO;
import com.qingchuan.coderman.admin.dao.AdminMapper;
import com.qingchuan.coderman.admin.entity.Admin;
import com.qingchuan.coderman.admin.entity.AdminExample;
import com.qingchuan.coderman.web.myenums.CustomizeErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 后台管理路由
 **/
@Controller
@RequestMapping("/admin")
@SuppressWarnings("all")
class AdminController {

    @Autowired
    private AdminMapper adminMapper;

    //后台登入
    @RequestMapping("/login")
    private String login(){
        return "admin/login";
    }

    //跳转到后台首页
    @GetMapping("/index")
    private String index(){
        return "admin/index";
    }

    //退出登入
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    private String logout(HttpSession session){
        session.invalidate();
        return "admin/login";
    }

    //管理员登入
    @ResponseBody
    @RequestMapping(value = "/doLogin",method = RequestMethod.POST)
    private Object doLogin(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("code") String code,
                           HttpServletRequest request){


        String adminLoginCode = (String) request.getSession().getAttribute("adminLoginCode");

        if(adminLoginCode==null||!adminLoginCode.equalsIgnoreCase(code)){
            return new ResultTypeDTO().errorOf(CustomizeErrorCode.CODE_NOT_SUCCESS);
        }
        AdminExample example = new AdminExample();
        example.createCriteria().andUsernameEqualTo(username)
                .andPasswordEqualTo(password);
        List<Admin> admins = adminMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(admins)){
            Admin admin = admins.get(0);
            request.getSession().setAttribute("admin",admin);
            return new ResultTypeDTO().okOf();
        }else {
            return new ResultTypeDTO().errorOf("用户名或密码错误");
        }
    }

}
