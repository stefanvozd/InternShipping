(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('ResumeDialogController', ResumeDialogController);

    ResumeDialogController.$inject = ['$location','$timeout', '$scope', '$stateParams', '$q', 'DataUtils', 'entity', 'Resume', 'User', 'Application'];

    function ResumeDialogController ($location,$timeout, $scope, $stateParams, $q, DataUtils, entity, Resume, User, Application) {
        var vm = this;

        vm.resume = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();
        vm.applications = Application.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
        }

        function save () {
            vm.isSaving = true;
            if (vm.resume.id !== null) {
                Resume.update(vm.resume, onSaveSuccess, onSaveError);
            } else {
                Resume.save(vm.resume, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('internShippingApp:resumeUpdate', result);
            $location.path("myresume");
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImage = function ($file, resume) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        resume.image = base64Data;
                        resume.imageContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.birthDate = false;

        vm.setCvFile = function ($file, resume) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        resume.cvFile = base64Data;
                        resume.cvFileContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
