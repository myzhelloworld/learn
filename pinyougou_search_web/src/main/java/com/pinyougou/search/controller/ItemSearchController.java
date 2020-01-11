package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zx
 * @version 1.0
 * @date 2020/1/11  12:52
 */
@RestController
@RequestMapping("itemSearch")
public class ItemSearchController {
    @Reference
    private ItemSearchService itemSearchService;

    @RequestMapping("search")
    public Map<String,Object> search( @RequestBody Map searchMap){
        return itemSearchService.search(searchMap);
    }
}
