(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('resume', {
            parent: 'entity',
            url: '/resume',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'internShippingApp.resume.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resume/resumes.html',
                    controller: 'ResumeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resume');
                    $translatePartialLoader.addPart('education');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('resume-detail', {
            parent: 'entity',
            url: '/resume/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'internShippingApp.resume.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resume/resume-detail.html',
                    controller: 'ResumeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resume');
                    $translatePartialLoader.addPart('education');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Resume', function($stateParams, Resume) {
                    return Resume.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('resume.new', {
            parent: 'resume',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resume/resume-dialog.html',
                    controller: 'ResumeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                image: null,
                                imageContentType: null,
                                name: null,
                                title: null,
                                overview: null,
                                education: null,
                                faculty: null,
                                enrollmentYear: null,
                                location: null,
                                contactEmail: null,
                                birthDate: null,
                                jsonResume: null,
                                cvFile: null,
                                cvFileContentType: null,
                                receiveJobAlerts: null,
                                socialLinkedin: null,
                                representativeSkills: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('resume', null, { reload: true });
                }, function() {
                    $state.go('resume');
                });
            }]
        })
        .state('resume.edit', {
            parent: 'resume',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resume/resume-dialog.html',
                    controller: 'ResumeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Resume', function(Resume) {
                            return Resume.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resume', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resume.delete', {
            parent: 'resume',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resume/resume-delete-dialog.html',
                    controller: 'ResumeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Resume', function(Resume) {
                            return Resume.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resume', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
