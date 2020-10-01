package com.qingchuan.coderman.web.service;

import com.qingchuan.coderman.web.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> list(Integer pageNo, Integer pageSize, Integer id);
}
