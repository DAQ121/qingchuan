package com.qingchuan.coderman.admin.vo;

import com.qingchuan.coderman.web.modal.User;
import lombok.Data;

/**
 * VO:值对象（Value Object）
 * 业务对象，存活在业务层，供业务逻辑使用，存活的目的就是为数据提供一个生存的地方。
 **/
@Data
@SuppressWarnings("all")
public class UserVO extends User {
    private int page;
    private int limit;
}
