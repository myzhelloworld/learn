//服务层
app.service('itemCatService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../itemCat/findAll.do');		
	}

	this.findByParentId=function(parentId){
		return $http.get("../itemCat/findByParentId.do?parentId="+parentId);
	}
});

