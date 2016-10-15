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
            url: '/resume?page&sort&search',
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
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resume');
                    $translatePartialLoader.addPart('education');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
            .state('myresume', {
            parent: 'entity',
            url: '/myresume',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'internShippingApp.resume.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resume/myresumes.html',
                    controller: 'ResumeController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
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
            .state('resumes-for-job', {
                parent: 'entity',
                url: '/job/{id}/resumes',
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
                params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'id,asc',
                        squash: true
                    },
                    search: null
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', 'ResumesJob', function ($stateParams, PaginationUtil, ResumesJob) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort),
                            search: $stateParams.search
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('resume');
                        $translatePartialLoader.addPart('education');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
        .state('resume.new', {
            parent: 'resume',
            url: '/new',
            data: {
                authorities: ['ROLE_CANDIDATE'],
                pageTitle: 'internShippingApp.company.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resume/resume-dialog.html',
                    controller: 'ResumeDialogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: function () {
                    return {
                        logo: null,
                        logoContentType: null,
                        name: null,
                        domain: null,
                        shortDescription: null,
                        longDescription: null,
                        website: null,
                        contactEmail: null,
                        contactNumber: null,
                        companySize: null,
                        foundedYear: null,
                        socialFacebook: null,
                        socialLinkedin: null,
                        socialYoutube: null,
                        socialTwitter: null,
                        address: null,
                        id: null
                    };
                }
            }
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
