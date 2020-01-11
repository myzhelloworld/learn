package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.search.service.ItemSearchService;
import entity.SolrItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zx
 * @version 1.0
 * @date 2020/1/11  12:41
 */
//设置请求超时间，dubbo默认是1秒
@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map search(Map searchMap) {
        Map map = new HashMap();
        //构造查询对象
        Query query = new SimpleQuery("*:*");
        //组装查询条件
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //分页查询
        ScoredPage<SolrItem> page = solrTemplate.queryForPage(query, SolrItem.class);
        //返回数据列表
        map.put("rows", page.getContent());
        return map;
    }
}
