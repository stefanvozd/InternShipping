(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('CompanyController', CompanyController);

    CompanyController.$inject = ['$scope', '$state', 'DataUtils', 'Company', 'CompanySearch'];

    function CompanyController ($scope, $state, DataUtils, Company, CompanySearch) {
        var vm = this;
        
        vm.companies = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Company.query(function(result) {
                vm.companies = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            CompanySearch.query({query: vm.searchQuery}, function(result) {
                vm.companies = result;
            });
        }    }
})();
