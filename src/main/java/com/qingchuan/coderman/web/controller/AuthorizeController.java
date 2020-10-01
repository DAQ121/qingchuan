package com.qingchuan.coderman.web.controller;

import com.qingchuan.coderman.web.dto.*;
import com.qingchuan.coderman.web.modal.User;
import com.qingchuan.coderman.web.myenums.UserType;
import com.qingchuan.coderman.web.provider.QQProvider;
import com.qingchuan.coderman.web.service.UserService;
import com.qingchuan.coderman.web.utils.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Controller
@SuppressWarnings("all")
public class AuthorizeController {

    protected static final Logger logger = LoggerFactory.getLogger(AuthorizeController.class);

    @Autowired
    private UserService userService;

    @Value("${chatId}")
    private String chatId;
    @Value("${chatKey}")
    private String chatKey;

    @Autowired
    private QQProvider qqProvider;

    @Value("${qq.client.id}")
    private String qq_clientId;

    @Value("${qq.client.secret}")
    private String qq_clientSecret;

    @Value("${qq.client.redirecturi}")
    private String qq_RedirectUri;

    // qq登录回调
    @GetMapping("/qqcallback")
    private String QQCallBack(@RequestParam(value = "code",required = false) String code, HttpServletRequest request, HttpServletResponse response) {
        QQAccessTokenDTO qqAccessTokenDTO = new QQAccessTokenDTO();
        qqAccessTokenDTO.setClient_id(qq_clientId);
        qqAccessTokenDTO.setRedirect_uri(qq_RedirectUri);
        qqAccessTokenDTO.setCode(code);
        qqAccessTokenDTO.setClient_secret(qq_clientSecret);
        qqAccessTokenDTO.setGrant_type("authorization_code");
        //获取access_token
        String accessToken = QQProvider.getAccessToken(qqAccessTokenDTO);
        //获取openId
        String openId=qqProvider.getOpenId(accessToken);
        //通过openId获取QQ用户信息
        QQUser qqUser=qqProvider.getUserInfo(openId,qq_clientId,accessToken);
        if (qqUser != null) {
            //登入成功
            User user = new User();
            user.setAccountId(String.valueOf(openId));
            user.setName(qqUser.getNickname());
            user.setLocation(qqUser.getProvince()+qqUser.getCity());
            user.setAvatarUrl(qqUser.getFigureurl_qq());
            user.setUserType(UserType.QQ.getCode());
            //token
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            userService.SaveOrUpdate(user);
            Cookie c = new Cookie("token", token);
            c.setMaxAge(3600 * 24);//设置cookie的时长为24小时(登入的token)
            response.addCookie(c);
            //登入成功
            //logger.info("用户登入成功:{}",githubUser);
            //注册聊天
            long time = System.currentTimeMillis();
            HttpSession session = request.getSession();
            session.setAttribute("xlm_wid", chatId);
            session.setAttribute("xlm_uid", openId);
            session.setAttribute("xlm_name", qqUser.getNickname());
            session.setAttribute("xlm_avatar", qqUser.getFigureurl_qq_1());
            session.setAttribute("xlm_time", time);
            String string = 14876 + "_" + openId + "_" + time + "_" + chatKey;
            string = AuthorizeController.encryptPasswordWithSHA512(string).toLowerCase();
            session.setAttribute("xlm_hash", string);
            //聊天cookie
            Cookie[] cookies = request.getCookies();
            Cookie chatCookie = CookieUtils.findCookieByName(cookies, "JSESSIONID");
            if(chatCookie!=null){
                chatCookie.setMaxAge(3600 * 24);
                session.setMaxInactiveInterval(3600 * 24);
                response.addCookie(chatCookie);
            }

            return "redirect:/";
        } else {
            //登入失败
            logger.error("用户登入失败:{}", qqUser);
            return "redirect:/";
        }

    }

    // 聊天消息加密
    public static String encryptPasswordWithSHA512(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");  //创建SHA512类型的加密对象
            messageDigest.update(password.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuffer strHexString = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xff & bytes[i]);
                if (hex.length() == 1) {
                    strHexString.append('0');
                }
                strHexString.append(hex);
            }
            String result = strHexString.toString();
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 退出登入
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.invalidate();
        //清除cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                }
            }
        }
        response.addCookie(new Cookie("token", null));
        return "redirect:/";
    }

}

