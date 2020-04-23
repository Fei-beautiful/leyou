package com.leyou.item.Controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecifcationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("spec")
public class SpecifcationController {

    @Autowired
    private SpecifcationService specifcationService;

    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> group  = specifcationService.queryGroupByCid(cid);
        if (CollectionUtils.isEmpty(group)){
         return    ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //404
        }
          return  ResponseEntity.ok(group);
    }

    /*
    根据分组ID查询 规格参数
    **/

    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParam(
            @RequestParam(value = "gid" ,required = false) Long gid,
            @RequestParam(value = "cid" ,required = false) Long cid,
            @RequestParam(value = "generic" ,required = false) Boolean generic,
            @RequestParam(value = "searching" ,required = false) Boolean searching
    ){
        List<SpecParam> Param  = specifcationService.queryParam(gid,cid,generic,searching);
        if (CollectionUtils.isEmpty(Param)){
            return    ResponseEntity.status(HttpStatus.NOT_FOUND).build(); //404
        }
        return  ResponseEntity.ok(Param);
    }


    }

