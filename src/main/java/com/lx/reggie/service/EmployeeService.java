package com.lx.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lx.reggie.common.R;
import com.lx.reggie.entity.Employee;

import javax.servlet.http.HttpServletRequest;


public interface EmployeeService extends IService<Employee> {
    R<Employee> Login(HttpServletRequest request, Employee employee);

    R<String> Logout(HttpServletRequest request);

    R<String> AddStaff(Employee employee, HttpServletRequest request);

    R<Page> StaffPagingQuery(int page, int pageSize, String name);

    R<String> UpdateById(Employee employee, HttpServletRequest request);

    R<Employee> getStaffById(Long id);
}
