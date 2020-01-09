package com.pinyougou.sellergoods.service;
import com.pinyougou.pojo.TbGoods;
import entity.PageResult;
import pojoGroup.Goods;

import java.util.List;

/**
 * 业务逻辑接口
 * @author Steven
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage( int pageNum, int pageSize );
	
	
	/**
	 * 增加
     * @param goods
     */
	public void add( Goods goods );
	
	
	/**
	 * 修改
	 */
	public void update(Goods goods );
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Goods findOne( Long id );
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete( Long[] ids );

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage( TbGoods goods, int pageNum, int pageSize );
	
}
