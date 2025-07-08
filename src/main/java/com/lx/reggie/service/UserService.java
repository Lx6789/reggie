package com.lx.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.reggie.common.R;
import com.lx.reggie.entity.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface UserService extends IService<User> {
    R<String> SendMsg(User user, HttpSession session);

    R<User> login(Map map, HttpSession session);
}
