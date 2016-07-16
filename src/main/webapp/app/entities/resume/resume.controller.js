(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('ResumeController', ResumeController);

    ResumeController.$inject = ['$scope', '$state', 'DataUtils', 'Resume', 'ResumeSearch'];

    function ResumeController ($scope, $state, DataUtils, Resume, ResumeSearch) {
        var vm = this;
        
        vm.resumes = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Resume.query(function(result) {
                vm.resumes = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ResumeSearch.query({query: vm.searchQuery}, function(result) {
                vm.resumes = result;
            });
        }    }
})();
