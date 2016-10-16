(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('JobDialogController', JobDialogController);

    JobDialogController.$inject = ['$location', '$timeout', '$scope', '$stateParams', 'entity', 'Job', 'Application', 'Company'];

    function JobDialogController ($location, $timeout, $scope, $stateParams, entity, Job, Application, Company) {
        var vm = this;

        vm.job = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.applications = Application.query();
        vm.companies = Company.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {

        }

        function save () {
            vm.isSaving = true;
            if (vm.job.id !== null) {
                Job.update(vm.job, onSaveSuccess, onSaveError);
            } else {
                Job.save(vm.job, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $location.path('ourjobs');
            $scope.$emit('internShippingApp:jobUpdate', result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;
        vm.datePickerOpenStatus.activeUntil = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
