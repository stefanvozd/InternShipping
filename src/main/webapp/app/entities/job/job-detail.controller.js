(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('JobDetailController', JobDetailController);

    JobDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Job', 'Application', 'Company'];

    function JobDetailController($scope, $rootScope, $stateParams, entity, Job, Application, Company) {
        var vm = this;

        vm.job = entity;

        var unsubscribe = $rootScope.$on('internShippingApp:jobUpdate', function(event, result) {
            vm.job = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
