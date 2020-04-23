package com.leyou.item.Controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.Spubo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<Spubo>> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows
    ) {
        PageResult<Spubo> result = this.goodsService.querySpuByPage(key, saleable, page, rows);
        if (CollectionUtils.isEmpty(result.getItems())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 根据ID 新增商品
     * */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spubo spubo) {

        this.goodsService.saveGoods(spubo);
        return ResponseEntity.status(HttpStatus.CREATED).build();  //201

    }

    /**
     * 根据Id查询SpuDetail
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
     public ResponseEntity<SpuDetail> qureySpuDteailById(@PathVariable("spuId") Long spuId){

         SpuDetail spuDetail = this.goodsService.qureySpuDteailById(spuId);
               if (spuDetail==null){
                return   ResponseEntity.notFound().build();
               }
                return   ResponseEntity.ok(spuDetail);
      }

    /**
     * 根据Id查询Sku；
     * @param spuId
     * @return
     */
      @GetMapping("sku/list")
      public ResponseEntity<List<Sku>>  qureySkuById(@RequestParam("id") Long spuId){
          List<Sku> skus = this.goodsService.qureySkuById(spuId);
          if (skus==null){
              return   ResponseEntity.notFound().build();
          }
          return   ResponseEntity.ok(skus);
      }

    /**
     * 更新商品信息
     */
    @PutMapping("goods")
    public ResponseEntity<Void> UpdateGoods(@RequestBody Spubo spubo){
              this.goodsService.UpdateGoods(spubo);
              return ResponseEntity.noContent().build();
    }


      }
