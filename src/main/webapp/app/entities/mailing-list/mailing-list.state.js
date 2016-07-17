(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mailing-list', {
            parent: 'entity',
            url: '/mailing-list',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'internShippingApp.mailingList.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mailing-list/mailing-lists.html',
                    controller: 'MailingListController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('mailingList');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('mailing-list-detail', {
            parent: 'entity',
            url: '/mailing-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'internShippingApp.mailingList.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mailing-list/mailing-list-detail.html',
                    controller: 'MailingListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('mailingList');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MailingList', function($stateParams, MailingList) {
                    return MailingList.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('mailing-list.new', {
            parent: 'mailing-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mailing-list/mailing-list-dialog.html',
                    controller: 'MailingListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                email: null,
                                dateCreated: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mailing-list', null, { reload: true });
                }, function() {
                    $state.go('mailing-list');
                });
            }]
        })
        .state('mailing-list.edit', {
            parent: 'mailing-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mailing-list/mailing-list-dialog.html',
                    controller: 'MailingListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MailingList', function(MailingList) {
                            return MailingList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mailing-list', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mailing-list.delete', {
            parent: 'mailing-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mailing-list/mailing-list-delete-dialog.html',
                    controller: 'MailingListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MailingList', function(MailingList) {
                            return MailingList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mailing-list', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
