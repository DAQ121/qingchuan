
package com.qingchuan.coderman.web.controller;

import ch.qos.logback.core.joran.action.ActionConst;
import com.github.pagehelper.PageInfo;
import com.qingchuan.coderman.web.dao.*;
import com.qingchuan.coderman.web.dto.NotificationDTO;
import com.qingchuan.coderman.web.dto.PeopleDetailsInfo;
import com.qingchuan.coderman.web.dto.ResultTypeDTO;
import com.qingchuan.coderman.web.modal.*;
import com.qingchuan.coderman.web.dao.*;
import com.qingchuan.coderman.web.exception.CustomizeException;
import com.qingchuan.coderman.web.myenums.CommentNotificationType;
import com.qingchuan.coderman.web.myenums.CommentStatus;
import com.qingchuan.coderman.web.myenums.CustomizeErrorCode;
import com.qingchuan.coderman.web.service.NotificationService;
import com.qingchuan.coderman.web.service.QuestionService;
import com.qingchuan.coderman.web.service.UserService;
import com.qingchuan.coderman.web.utils.CommunityUtil;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 个人信息
 */
@Controller
@SuppressWarnings("all")
public class ProfileController {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserExtMapper userExtMapper;

    //七牛云存储相关
    @Value("${qiniu.key.access}")
    private String accessKey;
    @Value("${qiniu.key.secret}")
    private String secretKey;
    @Value("${qiniu.bucket.header.name}")
    private String headerBucketName;
    @Value("${qiniu.bucket.header.url}")
    private String headerBucketUrl;


    @GetMapping("/setting")
    public String getSettingPage(Model model){
        //上传文件名称
        String fileName = CommunityUtil.generateUUID();
        //设置响应信息
        StringMap policy = new StringMap();
        policy.put("returnBody",CommunityUtil.getJSONString(0));
        //生成上传凭证
        Auth auth = Auth.create(accessKey,secretKey);
        String uploadTocken = auth.uploadToken(headerBucketName,fileName,3600,policy);
        model.addAttribute("uploadToken",uploadTocken);
        model.addAttribute("fileName",fileName);
        return "profile";
    }

    //更新头像路径
    @PostMapping("/header/url")
    @ResponseBody
    public String updateHeaderUrl(String fileName,HttpServletRequest request){
        if(StringUtils.isBlank(fileName)){
            return CommunityUtil.getJSONString(1,"文件名不能为空");
        }
        String url = headerBucketUrl + "/" +fileName;
        User user = (User) request.getSession().getAttribute("user");
        user.setAvatarUrl(url);
        userService.SaveOrUpdate(user);
        return CommunityUtil.getJSONString(0);
    }

    //转发到我的主页
    @GetMapping("/profile")
    public String toProfile(@RequestParam(value = "action",required = false) String action,HttpServletRequest request, Map<String, Object> map) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        if("replies".equals(action)){
            map.put("action",action);
        }
        //个人数据信息
        PeopleDetailsInfo peopleDetailsInfo = new PeopleDetailsInfo();
        peopleDetailsInfo.setFanCount(userExtMapper.getFansCount(user.getId()));
        peopleDetailsInfo.setFollowCount(userExtMapper.getFollowCount(user.getId()));
        peopleDetailsInfo.setQuestionCount(userExtMapper.getQuestionCount(user.getId()));
        peopleDetailsInfo.setCollectCount(userExtMapper.getCollectCount(user.getId()));
        if(user!=null){
            peopleDetailsInfo.setIntegral(userExtMapper.getIntegral(user.getId()));
        }
        map.put("peopleDetails",peopleDetailsInfo);
        map.put("people", user);
        return "profile";
    }
    //加载我的关注
    @GetMapping("/loadMyFollows")
    @ResponseBody
    public ResultTypeDTO loadMyFollows(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");

        List<User> followList = userService.getFollowList(user);
        return new ResultTypeDTO().okOf().addMsg("userList", followList);
    }
    //加载我的通知
    @GetMapping("/loadMyReplies")
    @ResponseBody
    public ResultTypeDTO loadMyReplies(@RequestParam(name = "pageSize", defaultValue = "15") Integer pageSize,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, HttpServletRequest request) {
        //获取通知信息
        User user = (User) request.getSession().getAttribute("user");
        if(user!=null){
            List<NotificationDTO> notificationDTOPageInfo = notificationService.list(pageNo, pageSize, user.getId());
            return new ResultTypeDTO().okOf().addMsg("notification", notificationDTOPageInfo);
        }
        return null;
    }
    //加载我的粉丝
    @GetMapping("/loadPeopleFans")
    @ResponseBody
    public ResultTypeDTO loadMyFans( HttpServletRequest request){
        //获取通知信息
        User user = (User) request.getSession().getAttribute("user");
        List<User> fans = userService.getFansList(user);
        return new ResultTypeDTO().okOf().addMsg("fans", fans);
    }
    //加载我的问题,我的收藏
    @GetMapping("/loadPeopleData/{action}")
    @ResponseBody
    public ResultTypeDTO loadPeopleData(@PathVariable("action") String action, Map<String, Object> map, HttpServletRequest request,
                                        @RequestParam(name = "pageSize", defaultValue = "15") Integer pageSize,
                                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        User user = (User) request.getSession().getAttribute("user");
        if ("questions".equals(action)) {
            PageInfo<Question> myquestionPageInfo = questionService.findQuestionsByUserId(pageNo, pageSize, user.getId());
            //获取通知信息
            return new ResultTypeDTO().okOf().addMsg("page", myquestionPageInfo);
        }


        //我的收藏
        if ("collects".equals(action)) {
            PageInfo<Question> questionPageInfo = questionService.getCollectPage(pageNo, pageSize, user.getId());
            return new ResultTypeDTO().okOf().addMsg("page", questionPageInfo);
        }
        if (!"follows".equals(action) && !"questions".equals(action) && !"replies".equals(action) && !"collects".equals(action)) {
            throw new CustomizeException(CustomizeErrorCode.PAGE_NOT_FOUNT);
        }
        //map.put("unreadcount", unreadcount);

        return null;
    }
    //查看最新通知
    @GetMapping("/read")
    public String getNotificationDetail(@RequestParam("id") Integer id,
                                        @RequestParam("status") Integer isread,
                                        HttpServletRequest request,Map<String,Object> map) {
        User user = (User) request.getSession().getAttribute("user");
        //修改通知为已读
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (!isread.equals(CommentStatus.READ.getCode())) {
            notification.setStatus(CommentStatus.READ.getCode());
            notificationMapper.updateByPrimaryKeySelective(notification);
            //未读信息数
            NotificationExample notificationExample = new NotificationExample();
            NotificationExample.Criteria criteria = notificationExample.createCriteria();
            criteria.andReceiverEqualTo((long) user.getId());
            criteria.andStatusEqualTo(CommentStatus.UN_READ.getCode());
            Integer unreadcount = notificationMapper.countByExample(notificationExample);
            request.getSession().setAttribute("unreadcount", unreadcount);
        }

        //找到关联问题的页面
        Integer type = notification.getType();

        if (type.equals(CommentNotificationType.COMMENT_QUESTION.getCode()) || type.equals(CommentNotificationType.LIKE_QUESTION.getCode())) {
            if (notification.getOutterId() == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            return "redirect:/question/" + notification.getOutterId();
        } else if (type.equals(CommentNotificationType.COMMENT_REPLY.getCode()) || type.equals(CommentNotificationType.COMMENT_Like.getCode())) {
            Comment comment = commentMapper.selectByPrimaryKey(notification.getOutterId());
            if (comment == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            Integer questionId = comment.getParentId();
            return "redirect:/question/" + questionId;
        }else if(type.equals(CommentNotificationType.FOLLOWING_YOU.getCode())){
            return "redirect:/people?id="+notification.getOutterId();
        }
        return "error";
    }
    // 删除通知
    @GetMapping("/deletenotification")
    public String deletenotification(@RequestParam("id") Integer id) {
        notificationMapper.deleteByPrimaryKey(id);
        return "redirect:/profile";
    }
    @GetMapping("/deleteNotification")
    @ResponseBody
    public ResultTypeDTO deleteNotificationById(@RequestParam("id") Integer id) {
        try {
            notificationMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            return new ResultTypeDTO().errorOf(CustomizeErrorCode.DELETE_NOTIFICATION_ERROR);
        }
        return new ResultTypeDTO().okOf();
    }

//    //删除已读
//     @GetMapping("/deleteReaded") public String delelteReaded(HttpServletRequest request) {
//     HttpSession session = request.getSession();
//     User user = (User) session.getAttribute("user");
//     if (user != null) {
//     NotificationExample notificationExample = new NotificationExample();
//     NotificationExample.Criteria criteria = notificationExample.createCriteria();
//     criteria.andReceiverEqualTo((long) user.getId());
//     c0riteria.andStatusEqualTo(CommentStatus.READ.getCode());
//     notificationMapper.deleteByExample(notificationExample);
//     }
//     return "redirect:/profile/replies";
//     }

    // 删除已读的通知
    @ResponseBody
    @GetMapping("/AjaxDeleteRead")
    public ResultTypeDTO AjaxDeleteRead(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            NotificationExample notificationExample = new NotificationExample();
            NotificationExample.Criteria criteria = notificationExample.createCriteria();
            criteria.andReceiverEqualTo((long) user.getId());
            criteria.andStatusEqualTo(CommentStatus.READ.getCode());
            notificationMapper.deleteByExample(notificationExample);
        }
        return new ResultTypeDTO().okOf();
    }
    // 全部已读
    @ResponseBody
    @GetMapping("/readAll")
    public ResultTypeDTO readAll(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            try {
                userExtMapper.readAllNotification(user.getId());
                return new ResultTypeDTO().okOf();
            } catch (Exception e) {
                return new ResultTypeDTO().errorOf(CustomizeErrorCode.READ_ALL_FAIL);
            }
        }
        return new ResultTypeDTO().errorOf(CustomizeErrorCode.READ_ALL_FAIL);
    }

}
