(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$location', '$scope', 'Principal', 'LoginService', '$state'];

    function HomeController ($location, $scope, Principal, LoginService, $state) {
        var vm = this;

        $scope.go = function ( path ,search) {
            console.log(path);
            $location.path(path).search('sort=_score,desc&search='+search);
        };

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
