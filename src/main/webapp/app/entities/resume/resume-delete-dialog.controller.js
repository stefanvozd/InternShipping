(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('ResumeDeleteController',ResumeDeleteController);

    ResumeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Resume'];

    function ResumeDeleteController($uibModalInstance, entity, Resume) {
        var vm = this;

        vm.resume = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Resume.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
