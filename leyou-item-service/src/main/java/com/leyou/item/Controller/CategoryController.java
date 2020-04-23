package com.leyou.item.Controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    // 根据父节点id查询子节点
    //查询分类，返回集合  RestFule风格的写法，作为统一返回类型

    @GetMapping("list")
    public ResponseEntity<List<Category>>  queryCategoryByPid(@RequestParam(value = "pid",required = true,defaultValue = "0") Long pid){
        try {
            if(pid==null || pid<0){
                //返回400  参数不
                return  ResponseEntity.status(HttpStatus.MULTI_STATUS).build();
            }
            List<Category>  categorys = categoryService.queryCategoryByPid(pid);
            if (CollectionUtils.isEmpty(categorys)){
                //404 服务器资源未找到
                return  ResponseEntity.status(HttpStatus.MULTI_STATUS).build();
            }
            // 200 ： 成功
            return ResponseEntity.ok(categorys);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //500 ：服务器内容错误
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
