package pojoGroup;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * @author zx
 * @version 1.0
 * @date 2020/1/1  16:40
 */
public class Specification implements Serializable {
    TbSpecification specification;
    List<TbSpecificationOption> specificationOptionList;

    public TbSpecification getSpecification() {
        return specification;
    }

    public void setSpecification( TbSpecification specification ) {
        this.specification = specification;
    }

    public List <TbSpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList( List <TbSpecificationOption> specificationOptionList ) {
        this.specificationOptionList = specificationOptionList;
    }
}
