(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('MailingListDeleteController',MailingListDeleteController);

    MailingListDeleteController.$inject = ['$uibModalInstance', 'entity', 'MailingList'];

    function MailingListDeleteController($uibModalInstance, entity, MailingList) {
        var vm = this;

        vm.mailingList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MailingList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
