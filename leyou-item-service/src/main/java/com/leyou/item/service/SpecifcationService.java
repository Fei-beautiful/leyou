package com.leyou.item.service;

import com.leyou.item.mapper.ParamMapper;
import com.leyou.item.mapper.SpecifcationMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SpecifcationService {

    @Autowired
    private SpecifcationMapper specifcationMapper;

    @Autowired
    private ParamMapper paramMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) {

        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        return this.specifcationMapper.select(specGroup);

    }
    /*
       根据分组ID查询 规格参数
       **/
    public List<SpecParam> queryParam(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam Param = new SpecParam();
        Param.setGroupId(gid);
        Param.setCid(cid);
        Param.setGeneric(generic);
        Param.setSearching(searching);

        return paramMapper.select(Param);
    }
}
