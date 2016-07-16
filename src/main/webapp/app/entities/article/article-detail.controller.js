(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('ArticleDetailController', ArticleDetailController);

    ArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Article'];

    function ArticleDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Article) {
        var vm = this;

        vm.article = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('internShippingApp:articleUpdate', function(event, result) {
            vm.article = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
