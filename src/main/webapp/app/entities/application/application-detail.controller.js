(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('ApplicationDetailController', ApplicationDetailController);

    ApplicationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Application', 'Resume', 'Job'];

    function ApplicationDetailController($scope, $rootScope, $stateParams, entity, Application, Resume, Job) {
        var vm = this;

        vm.application = entity;

        var unsubscribe = $rootScope.$on('internShippingApp:applicationUpdate', function(event, result) {
            vm.application = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
