package com.lx.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.reggie.common.R;
import com.lx.reggie.entity.User;
import com.lx.reggie.mapper.UserMapper;
import com.lx.reggie.service.UserService;
import com.lx.reggie.utils.SMSUtils;
import com.lx.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public R<String> SendMsg(User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //生成随机四位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info(code);
            //调用阿里云提供的短信服务
            //SMSUtils.sendMessage("瑞吉外卖", "", phone, code);//templateCode:模板code
            //将验证码保存到Session
            session.setAttribute(phone, code);
            return R.success("手机验证码发送成功");
        }
        return R.error("短信发送失败");
    }

    @Override
    public R<User> login(Map map, HttpSession session) {
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从Session获取保存的验证码
        Object codeInSession = session.getAttribute(phone);
        //进行验证码的比对
        if (codeInSession != null && codeInSession.equals(code)) {
            //如果能够比对成功，则登陆成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = this.getOne(queryWrapper);
            if (user == null) {
                //判断手机号是否在用户表中，若不在，则自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                this.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登陆失败");
    }
}
