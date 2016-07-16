(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('ApplicationController', ApplicationController);

    ApplicationController.$inject = ['$scope', '$state', 'Application', 'ApplicationSearch'];

    function ApplicationController ($scope, $state, Application, ApplicationSearch) {
        var vm = this;
        
        vm.applications = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Application.query(function(result) {
                vm.applications = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ApplicationSearch.query({query: vm.searchQuery}, function(result) {
                vm.applications = result;
            });
        }    }
})();
