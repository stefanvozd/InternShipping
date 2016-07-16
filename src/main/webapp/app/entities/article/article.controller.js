(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('ArticleController', ArticleController);

    ArticleController.$inject = ['$scope', '$state', 'DataUtils', 'Article', 'ArticleSearch'];

    function ArticleController ($scope, $state, DataUtils, Article, ArticleSearch) {
        var vm = this;
        
        vm.articles = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Article.query(function(result) {
                vm.articles = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ArticleSearch.query({query: vm.searchQuery}, function(result) {
                vm.articles = result;
            });
        }    }
})();
