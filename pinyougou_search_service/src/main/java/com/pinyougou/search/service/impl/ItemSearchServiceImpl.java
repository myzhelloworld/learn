package com.pinyougou.search.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.search.service.ItemSearchService;
import entity.SolrItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zx
 * @version 1.0
 * @date 2020/1/11  12:41
 */
//设置请求超时间，dubbo默认是1秒
@Service(timeout=5000)
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;
   @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Map search(Map searchMap) {
        Map map = searchList(searchMap);

        List <String> categoryList = searchCategoryList(searchMap);
        map.put("categoryList",categoryList);
        if(!StringUtils.isEmpty(searchMap.get("category"))){
            map.putAll(searchBrandAndSpecList((String)searchMap.get("category")));
        }
        if(categoryList.size()>0){
            map.putAll(searchBrandAndSpecList(categoryList.get(0)));
        }
        return map;
    }

    private Map searchList( Map searchMap ) {
        Map map = new HashMap();
        //        2.   构建query高亮查询对象new SimpleHighlightQuery
        HighlightQuery query = new SimpleHighlightQuery();
//        3.   复制之前的Criteria组装查询条件的代码
        //组装查询条件
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        if(!StringUtils.isEmpty(searchMap.get("category"))){
            Criteria filterCriteria=new Criteria("item_category").is(searchMap.get("category"));
            FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        if(!StringUtils.isEmpty(searchMap.get("brand"))){
            Criteria filterCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
            FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
            query.addFilterQuery(filterQuery);
        }
        if(searchMap.get("spec")!=null){
            Map<String,String> spec = (Map) searchMap.get("spec");
            for (String key: spec.keySet()) {
                Criteria filterCriteria = new Criteria("item_spec_" + key).is(spec.get(key));
                FilterQuery filterQuery=new SimpleFacetQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
//        4.   调用query.setHighlightOptions()方法，构建高亮数据三步曲：addField(高亮业务域)，
//              .setSimpleP..(前缀)，.setSimpleP..(后缀)
        HighlightOptions hOptions = new HighlightOptions().addField("item_title");
        hOptions.setSimplePrefix("<em style='color:red;'>");
        hOptions.setSimplePostfix("</em>");
        query.setHighlightOptions(hOptions);

//        1.   调用solrTemplate.queryForHighlightPage(query,class)方法，高亮查询数据
//        5.   接收solrTemplate.queryForHighlightPage的返回数据，定义page变量
        HighlightPage<SolrItem> page = solrTemplate.queryForHighlightPage(query, SolrItem.class);
//        6.   遍历解析page对象，page.getHighlighted().for，item = h.getEntity()，
//       item.setTitle(h.getHighlights().get(0).getSnipplets().get(0))，在设置高亮之前最好判断一下;
        for (HighlightEntry<SolrItem> h : page.getHighlighted()) {
            SolrItem item = h.getEntity();
            //设置高亮数据
            if(h.getHighlights().size() > 0 && h.getHighlights().get(0).getSnipplets().size() > 0) {
                item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));
            }
        }

//        7.   在循环完成外map.put("rows", page.getContent())返回数据列表
        map.put("rows", page.getContent());
        return map;
    }
    private List<String> searchCategoryList(Map searchMap){
        List<String> categoryList=new ArrayList <>();
        Query query = new SimpleQuery();
//        2.   复制之前的Criteria组装查询条件的代码
        //组装查询条件
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
//        3.   创建分组选项对象new GroupOptions().addGroupByField(域名)
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
//        4.   设置分组对象query.setGroupOptions
        query.setGroupOptions(groupOptions);
        //        5.   得到分组页对象page = solrTemplate.queryForGroupPage
        GroupPage<SolrItem> page = solrTemplate.queryForGroupPage(query, SolrItem.class);
//        6.   得到分组结果集groupResult = page.getGroupResult(域名)
        GroupResult<SolrItem> groupResult = page.getGroupResult("item_category");
//        7.   得到分组结果入口groupEntries = groupResult.getGroupEntries()
        Page<GroupEntry<SolrItem>> groupEntries = groupResult.getGroupEntries();
//        8.   得到分组入口集合content = groupEntries.getContent()
        List<GroupEntry<SolrItem>> content = groupEntries.getContent();
//        9.   遍历分组入口集合content.for(entry)，记录结果entry.getGroupValue()
        for (GroupEntry<SolrItem> entry : content) {
            categoryList.add(entry.getGroupValue());
        }
        return categoryList;
    }

    /**
     * 跟据商品分类名称查询商品品牌与规格列表
     * @param category 商品分类名称
     * @return
     */
    private Map searchBrandAndSpecList(String category){
        Map map = new HashMap();
        //跟据商品分类名称查询模板id
        Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
        if (typeId != null) {
            //跟据模板ID查询品牌列表
            List brandList = (List) redisTemplate.boundHashOps("brandList").get(typeId);
            map.put("brandList", brandList);
            //跟据模板ID查询品牌列表
            List specList = (List) redisTemplate.boundHashOps("specList").get(typeId);
            map.put("specList", specList);
        }
        return map;
    }
}
