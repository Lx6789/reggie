package com.lx.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lx.reggie.common.R;
import com.lx.reggie.entity.Employee;
import com.lx.reggie.mapper.EmployeeMapper;
import com.lx.reggie.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeService employeeService;

    @Override
    public R<Employee> Login(HttpServletRequest request, Employee employee) {
        //1.将页面提交的密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据用户名查数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3.如果没有查询到则返回登陆失败
        if (emp == null) {
            return R.error("登陆失败");
        }
        //4.密码比对
        if (!(emp.getPassword().equals(password))) {
            return R.error("登陆失败");
        }
        //5.查看状态
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        //6.登陆成功,将id存入Session并返回登陆成功
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    @Override
    public R<String> Logout(HttpServletRequest request) {
        //清楚Session中保存的当前登录员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    @Override
    public R<String> AddStaff(Employee employee, HttpServletRequest request) {
        //设置默认密码123456并进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());
        //获得挡墙登录用户的id
        //Long empId = (Long)request.getSession().getAttribute("employee");
        //employee.setCreateUser(empId);
        //employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    @Override
    public R<Page> StaffPagingQuery(int page, int pageSize, String name) {
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @Override
    public R<String> UpdateById(Employee employee, HttpServletRequest request) {
        //employee.setUpdateTime(LocalDateTime.now());
        //employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @Override
    public R<Employee> getStaffById(Long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("未查询到对应员工信息");
    }


}
