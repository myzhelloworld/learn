app.controller("searchController",function ($scope,searchService) {
    //搜索
    $scope.search=function(){
        searchService.search( $scope.searchMap ).success(
            function(response){
                //搜索返回的结果
                $scope.resultMap=response;
            }
        );
    }
    $scope.searchMap={'keywords':"",'category':"",'brand':"","spec":{}};

    $scope.addSearchItem=function (key, value) {
        if(key=='category'|| key=='brand'){
            $scope.searchMap[key]=value;
        }else {
            $scope.searchMap.spec[key]=value;
        }
        $scope.search();//执行搜索
    }
    //设计为单选
    $scope.removeSearchItem=function (key) {
        if(key=='category'|| key=='brand'){
            $scope.searchMap[key]="";
        }else{
         delete $scope.searchMap.spec[key];
        }
        $scope.search();//执行搜索
    }
})