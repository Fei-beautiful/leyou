package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;
    /**
     *
     * */
    public PageResult<Brand> queryBrandByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //  初始化Example对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria(); // 条件对象
        // key: 根据name模糊查询，或者根据首字母查询
            if (StringUtils.isNotBlank(key)){
                criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);

            }
          //添加分页条件
        PageHelper.startPage(page,rows);


        //添加排序条件
        if (StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+" "+(desc?"desc":"asc"));
        }
        List<Brand> brands = this.brandMapper.selectByExample(example);


        //把查询结果包装成PageInfo
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        // 包装成分页结果集进行返回
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    // 新增品牌
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {

        //向brand表中保存数据
        //如果以下结果为true，说明保存成功
       Boolean flag = this.brandMapper.insertSelective(brand) ==1;

       // 在向中间表保存数据
        if (flag){
            cids.forEach(cid->{
                this.brandMapper.insertCategoryAndBrand(cid,brand.getId());
            });
        }

    }

    /**
     * 根据商品分类查询所属品牌
     */
    public List<Brand> queryBrandByIds(Long cid) {
        return this.brandMapper.queryBrandByIds(cid);
    }
}
