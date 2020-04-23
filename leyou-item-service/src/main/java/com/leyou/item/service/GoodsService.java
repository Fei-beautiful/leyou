package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.Spubo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkuMapper SkuMapper;

    @Autowired
    private StockMapper StockMapper;


    public PageResult<Spubo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {

        Example example = new Example(Spu.class);
        //拼接查询条件的对象
        Example.Criteria criteria = example.createCriteria();
        // 添加查询条件
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }

        //添加上下架的过滤条件
         if (saleable!=null){
             criteria.andEqualTo("saleable",saleable);
         }

         //添加分页
       PageHelper.startPage(page,rows);

        //执行查询，获取spu集合
        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);

        //将spu集合转化为Spubo集合
      List<Spubo> spubos = spus.stream().map(spu -> {
          Spubo spubo = new Spubo();
          //将spu对象的所有属性拷贝给spubo对象
          BeanUtils.copyProperties(spu,spubo);
          // 查询品牌名称
          Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
          spubo.setBname(brand.getName());
          // 查询分类名称

          List<String> names = categoryService.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
          spubo.setCname(StringUtils.join(names,"/"));
          return  spubo;
      }).collect(Collectors.toList());

        //返回PageResule<spubo>
        return new PageResult<>(pageInfo.getTotal(),spubos);
    }

    /**
     * 新增商品
     * */
    @Transactional
    public void saveGoods(Spubo spubo) {
      // 先新增spu
        spubo.setId(null);
        spubo.setSaleable(true);
        spubo.setValid(true);
        spubo.setCreateTime(new Date());
        spubo.setLastUpdateTime(spubo.getCreateTime());
        this.spuMapper.insertSelective(spubo);

        //在新增spuDetail
        SpuDetail spuDetail = spubo.getSpuDetail();
        spuDetail.setSpuId(spubo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);
        //在新曾sku
        //先将浏览器传过来的sku集合遍历成sku，然后新增
        saveSku(spubo);
    }

    public void saveSku(Spubo spubo) {
        spubo.getSkus().forEach(sku -> {
            //新增sku
            sku.setId(null);
            sku.setSpuId(spubo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.SkuMapper.insertSelective(sku);

         //在新增stock
          Stock stock = new Stock();
          stock.setSkuId(sku.getId());
          stock.setStock(sku.getStock());
          this.StockMapper.insertSelective(stock);
         });
    }

    /**
     * 根据Id查询SpuDetail
     * @param spuId
     * @return
     */
    public SpuDetail qureySpuDteailById(Long spuId) {
        return  this.spuDetailMapper.selectByPrimaryKey(spuId);

    }
    /**
     * 根据Id查询Sku；
     * @param spuId
     * @return
     */
    public List<Sku> qureySkuById(Long spuId) {
        //创建一个商品表
        Sku sku = new Sku();
        //根据商品表的SpuId查询
        sku.setSpuId(spuId);
        //查询后，返回一个List集合
        List<Sku> select = this.SkuMapper.select(sku);
        // forEach遍历集合，在获取库存里面的信息，返回的值放入到商品表里
        select.forEach(sku1 ->{
            Stock stock = this.StockMapper.selectByPrimaryKey(sku1.getId());
            sku1.setStock(stock.getStock());
        } );
      //返回商品表接受的数据。
        return select;
    }

    /**
     * 更新商品信息
     */
    public void UpdateGoods(Spubo spubo) {

        //一个商品有多个sku
        //创建一个库存量商品，给Sku商品赋值spu商品对应的数据
        Sku sku = new Sku();
        sku.setSpuId(spubo.getId());
        //然后查询出来，
        List<Sku> skuList = this.SkuMapper.select(sku);
        // 遍历一下，在删除掉stock
        skuList.forEach(sku1 -> {
            this.StockMapper.deleteByPrimaryKey(sku1.getId());
        });
        //删除sku
        Sku sku1 = new Sku();
        sku1.setSpuId(spubo.getId());
        this.SkuMapper.delete(sku1);
        //新增sku和新增stock
        saveSku(spubo);

        //更新spu和spuDetail
        this.spuMapper.updateByPrimaryKeySelective(spubo);
        this.spuDetailMapper.updateByPrimaryKeySelective(spubo.getSpuDetail());
        //不能更改的属性
        spubo.setCreateTime(null);
        spubo.setLastUpdateTime(new Date());
        spubo.setSaleable(null);
        spubo.setValid(null);


    }
}
