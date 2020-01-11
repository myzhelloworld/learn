app.controller("indexController",function ($scope,contentService) {

    //声明广告数据:[分类id:[数据列表]]
    $scope.contentList=[];

    $scope.findByCategoryId=function(categoryId){
        contentService.findByCategoryId(categoryId).success(
            function(response){
                $scope.contentList[categoryId]=response;
            }
        );
    }

    $scope.keywords = "";

    $scope.search=function () {
        if($scope.keywords == ""){
            alert("请先输入搜索关键字");
            return;
        }
        window.location.href = "http://localhost:8084/search.html#?keywords="+$scope.keywords;
    }
});