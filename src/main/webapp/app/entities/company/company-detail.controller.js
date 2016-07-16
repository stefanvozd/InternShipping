(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('CompanyDetailController', CompanyDetailController);

    CompanyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Company', 'User', 'Job'];

    function CompanyDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Company, User, Job) {
        var vm = this;

        vm.company = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('internShippingApp:companyUpdate', function(event, result) {
            vm.company = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
