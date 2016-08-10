(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('CompanyDialogController', CompanyDialogController);

    CompanyDialogController.$inject = ['$timeout', '$uibModal', '$location', '$scope', '$stateParams',  '$q', 'DataUtils', 'entity', 'Company', 'User', 'Job', 'Notification'];

    function CompanyDialogController ($timeout, $uibModal, $location, $scope, $stateParams,  $q, DataUtils, entity, Company, User, Job, Notification) {
        var vm = this;
        
        vm.company = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();
        vm.jobs = Job.query();
        
        $('.dropify').dropify();
        
     // Summernote WYSIWYG
       
          $('.summernote-editor').summernote({
            dialogsInBody: true,
            height: 300
          });
        
        
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
        }

        function save () {
            vm.isSaving = true;
            if (vm.company.id !== null) {
                Company.update(vm.company, onSaveSuccess, onSaveError);
            } else {
                Company.save(vm.company, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('internShippingApp:companyUpdate', result);     
            
            $location.url("company");
            
            Notification.success('<span translate="global.messages.saved">Saved!</span>');
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setLogo = function ($file, company) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        company.logo = base64Data;
                        company.logoContentType = $file.type;
                    });
                });
            }
        };

    }
})();


