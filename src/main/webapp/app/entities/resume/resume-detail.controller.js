(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('ResumeDetailController', ResumeDetailController);

    ResumeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Resume', 'User', 'Application'];

    function ResumeDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Resume, User, Application) {
        var vm = this;

        vm.resume = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('internShippingApp:resumeUpdate', function(event, result) {
            vm.resume = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
