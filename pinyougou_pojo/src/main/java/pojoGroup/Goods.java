package pojoGroup;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author zx
 * @version 1.0
 * @date 2020/1/4  12:59
 */
public class Goods implements Serializable{
    private TbGoods goods;
    private TbGoodsDesc goodsDesc;
    private List<TbItem> itemList;

    public TbGoods getGoods() {
        return goods;
    }

    public void setGoods( TbGoods goods ) {
        this.goods = goods;
    }

    public TbGoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc( TbGoodsDesc goodsDesc ) {
        this.goodsDesc = goodsDesc;
    }

    public List <TbItem> getItemList() {
        return itemList;
    }

    public void setItemList( List <TbItem> itemList ) {
        this.itemList = itemList;
    }
}
