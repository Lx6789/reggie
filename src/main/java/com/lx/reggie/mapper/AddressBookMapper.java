package com.lx.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lx.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
