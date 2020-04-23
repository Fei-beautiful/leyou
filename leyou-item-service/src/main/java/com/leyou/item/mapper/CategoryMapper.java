package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;


// Category 是返回值得类型
 // Long  主键的类型
public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {
}
