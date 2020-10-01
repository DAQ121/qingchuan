package com.qingchuan.coderman.web.advice;

import org.springframework.beans.factory.annotation.Value;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener //监听注解
@SuppressWarnings("all")
public class WebStartListener implements ServletContextListener {

    //qq
    @Value("${qq.client.id}")
    private String qq_Client_Id;
    @Value("${qq.client.secret}")
    private String qq_Client_Secret;
    @Value("${qq.client.redirecturi}")
    private String qq_Redirect_Uri;
    @Value("${index.qq_Url}")
    private String qq_loginUrl;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //qq授权登入
        ServletContext servletContext = servletContextEvent.getServletContext();
        qq_loginUrl=qq_loginUrl+"?response_type=code&client_id="+qq_Client_Id+"&redirect_uri="+qq_Redirect_Uri+"&scope=scope&display=display";
        servletContext.setAttribute("qq_loginUrl",qq_loginUrl);
        System.out.println(qq_loginUrl);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) { }
}
