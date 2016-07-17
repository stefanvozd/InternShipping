(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('MailingListDialogController', MailingListDialogController);

    MailingListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MailingList'];

    function MailingListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MailingList) {
        var vm = this;

        vm.mailingList = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mailingList.id !== null) {
                MailingList.update(vm.mailingList, onSaveSuccess, onSaveError);
            } else {
                MailingList.save(vm.mailingList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('internShippingApp:mailingListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateCreated = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
