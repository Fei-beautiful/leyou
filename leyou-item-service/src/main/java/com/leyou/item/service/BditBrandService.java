package com.leyou.item.service;

import com.leyou.item.mapper.BditBrandMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BditBrandService {

    @Autowired
    private BditBrandMapper bditBrandMapper;

    public List<Category> queryByBrandId(Long bid) {
        return this.bditBrandMapper.queryByBrandId(bid);
    }
}
