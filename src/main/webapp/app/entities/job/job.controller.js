(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('JobController', JobController);

    JobController.$inject = ['$scope', '$state', 'Job', 'JobSearch'];

    function JobController ($scope, $state, Job, JobSearch) {
        var vm = this;
        
        vm.jobs = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Job.query(function(result) {
                vm.jobs = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            JobSearch.query({query: vm.searchQuery}, function(result) {
                vm.jobs = result;
            });
        }    }
})();
