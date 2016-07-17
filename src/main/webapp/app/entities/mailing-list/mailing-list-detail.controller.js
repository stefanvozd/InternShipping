(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('MailingListDetailController', MailingListDetailController);

    MailingListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'MailingList'];

    function MailingListDetailController($scope, $rootScope, $stateParams, entity, MailingList) {
        var vm = this;

        vm.mailingList = entity;

        var unsubscribe = $rootScope.$on('internShippingApp:mailingListUpdate', function(event, result) {
            vm.mailingList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
