package com.leyou.item.Controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("brand")
public class BrandCortroller {

    @Autowired
    private BrandService brandService;


    /**
     * 根据查询条件分页并排序查询品牌信息
     * */

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>>  queryBrandsByPage(
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean  desc
    ){
        //通过条用service中方法查询品牌的分页结果集
    PageResult<Brand>  result = this.brandService.queryBrandByPage(key,page,rows,sortBy,desc);
     if (result == null || CollectionUtils.isEmpty(result.getItems())){
         //  如果查询没有数据就404
         return  ResponseEntity.notFound().build();  // 404
     }
     //  如果查询有数据就返回
     return  ResponseEntity.ok(result);
    }

    // 新增品牌
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids){

        this.brandService.saveBrand(brand,cids);
        return  ResponseEntity.status(HttpStatus.CREATED).build();   // 200
    }

    /**
     * 根据商品分类查询所属品牌
     */

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>>  queryBrandByIds(@PathVariable("cid") Long cid){
        List<Brand>  Brands  = brandService.queryBrandByIds(cid);
        if ( CollectionUtils.isEmpty(Brands)){
            //  如果查询没有数据就404
            return  ResponseEntity.notFound().build();  // 404
        }
        //  如果查询有数据就返回
        return  ResponseEntity.ok(Brands);
    }

    }


