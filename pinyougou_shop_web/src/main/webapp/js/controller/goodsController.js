 //控制层 
app.controller('goodsController' ,function($scope,$controller,goodsService,$location,uploadService,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
    $scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}}
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}
    $scope.image_entity={color:"",url:""};
    //上传图片
    $scope.uploadFile=function () {
        uploadService.uploadFile().success(function (response) {
            //如果上传成功,绑定url到表单
            if(response.success){
                $scope.image_entity.url=response.msg;
            }else{
                alert(response.msg);
            }
        }).error(function() {
            alert("上传发生错误");
        });
    }
    $scope.getItemCatList1=function () {
		itemCatService.findByParentId(0).success(function (response) {
			$scope.itemCatList1=response;

        })
    }

    $scope.$watch('entity.goods.category1Id',function (newValue,oldValue) {
		itemCatService.findByParentId(newValue).success(function (response) {
			$scope.itemCatList2=response;
            $scope.itemCatList3=[];
            $scope.entity.goods.typeTemplateId=$scope.findStr($scope.itemCatList1,'id',newValue,'typeId');

        })
    })
	$scope.$watch('entity.goods.category2Id',function (newValue, oldValue) {
        itemCatService.findByParentId(newValue).success(function (response) {
            $scope.itemCatList3=response;
            $scope.entity.goods.typeTemplateId=$scope.findStr($scope.itemCatList2,'id',newValue,'typeId');
        })
    })
	$scope.$watch('entity.goods.category3Id',function (newValue, oldValue) {
        $scope.entity.goods.typeTemplateId=$scope.findStr($scope.itemCatList3,'id',newValue,'typeId');
    })

    $scope.$watch("entity.goods.typeTemplateId",function (newValue, oldValue) {
		typeTemplateService.findOne(newValue).success(function (response) {
			$scope.brandIds=JSON.parse(response.brandIds);//注意返回的是字符串，需要自己解析为其他数据结构
            if($location.search()['id']==null){
                $scope.entity.goodsDesc.customAttributeItems=JSON.parse( response.customAttributeItems);
            }
        })
		typeTemplateService.findSpecList(newValue).success(function (response) {
			$scope.specList=response;
        })
    })

	$scope.updateSpecList=function ($event,name,value) {
        $scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0' }];//每次都重新初始化
    	var object = $scope.findMap($scope.entity.goodsDesc.specificationItems,"attributeName",name);
    	if(object){
            if($event.target.checked){
            	object.attributeValue.push(value);
            }
            else{
				var value_index = object.attributeValue.indexOf(value);
				object.attributeValue.splice(value_index,1);
				if(object.attributeValue.length==0){
					var obj_index = $scope.entity.goodsDesc.specificationItems.indexOf(object);
					$scope.entity.goodsDesc.specificationItems.splice(obj_index,1);
				}
            }
		}
		else{
    		$scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
		}

    }

    $scope.createItemList=function () {
        var items=$scope.entity.goodsDesc.specificationItems;
        for(var i=0;i<items.length;i++){
        	$scope.entity.itemList=this.addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
		}
    }
    $scope.addColumn=function (list,columnName,columnValue) {
		var newlist=[];
		for(var i=0;i<list.length;i++){
				for(var j=0;j<columnValue.length;j++){
					var newRow=JSON.parse(JSON.stringify(list[i]));
					newRow.spec[columnName]=columnValue[j];//插入新值
					newlist.push(newRow);
				}
		}
		return newlist;
    }
    //分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(){
        var id= $location.search()['id'];//获取参数值
        if(id==null){
            return ;
        }
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
				editor.html($scope.entity.goodsDesc.introduction);
                //显示图片列表
                $scope.entity.goodsDesc.itemImages=
                    JSON.parse($scope.entity.goodsDesc.itemImages);
                //显示扩展属性
                $scope.entity.goodsDesc.customAttributeItems=
					JSON.parse($scope.entity.goodsDesc.customAttributeItems);

                //规格
				$scope.entity.goodsDesc.specificationItems=
					JSON.parse($scope.entity.goodsDesc.specificationItems);

                for( var i=0;i<$scope.entity.itemList.length;i++ ){
                    $scope.entity.itemList[i].spec =
                        JSON.parse( $scope.entity.itemList[i].spec);
                }
            }
		);				
	}

	$scope.checkSelected=function (name,value) {
			var item=$scope.entity.goodsDesc.specificationItems;
			var spec= $scope.findMap(item,"attributeName",name);
			if(spec==null){
				return false;
			}
			if(spec.attributeValue.indexOf(value)>=0){
				return true;
			}
			// for(var i=0;i<spec.attributeValue.length;i++){
			// 	if(spec.attributeValue[i]==value){
			// 		return true;
			// 	}
			// }
			return false;
    }


    //添加图片列表
    $scope.add_image_entity=function(){
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }
    //列表中移除图片
    $scope.remove_image_entity=function(index){
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象
        $scope.entity.goodsDesc.introduction=editor.html();
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
                    $scope.entity={};
                    editor.html('');//清空富文本编辑器
                    location.href="goods.html";//跳转到商品列表页
				}else{
					alert(response.msg);
				}
			}		
		);				
	}


	//批量删除
	$scope.dele=function(){
		//获取选中的复选框
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}
			}
		);
	}

	$scope.searchEntity={};//定义搜索对象

	//搜索
	$scope.search=function(page,rows){
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}
		);
	}
	$scope.itemCatList=[];
	$scope.status=["未审核","已审核","审核未通过","关闭"];
	$scope.findItemCatList=function () {
		itemCatService.findAll().success(function (response) {
			for(var i=0;i<response.length;i++){
				$scope.itemCatList[response[i].id]=response[i].name;
			}
        })
    }

});	
