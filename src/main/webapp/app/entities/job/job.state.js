(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('job', {
            parent: 'entity',
            url: '/job?page&sort&search',
            data: {
                authorities: [],
                pageTitle: 'internShippingApp.job.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job/jobs.html',
                    controller: 'JobController',
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
                    $translatePartialLoader.addPart('job');
                    $translatePartialLoader.addPart('jobType');
                    $translatePartialLoader.addPart('jobLevel');
                    $translatePartialLoader.addPart('education');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
            .state('ourjobs', {
                parent: 'entity',
                url: '/ourjobs',
                data: {
                    authorities: ['ROLE_COMPANY'],
                    pageTitle: 'internShippingApp.resume.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/job/ourjobs.html',
                        controller: 'JobController',
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
                        $translatePartialLoader.addPart('job');
                        $translatePartialLoader.addPart('education');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('jobsapplied', {
                parent: 'entity',
                url: '/jobs/applied',
                data: {
                    authorities: ['ROLE_CANDIDATE'],
                    pageTitle: 'internShippingApp.resume.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/job/jobsapplied.html',
                        controller: 'JobController',
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
                        $translatePartialLoader.addPart('job');
                        $translatePartialLoader.addPart('education');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
        .state('job-detail', {
            parent: 'entity',
            url: '/job/{id}',
            data: {
                authorities: [],
                pageTitle: 'internShippingApp.job.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job/job-detail.html',
                    controller: 'JobDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('job');
                    $translatePartialLoader.addPart('jobType');
                    $translatePartialLoader.addPart('jobLevel');
                    $translatePartialLoader.addPart('education');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Job', function($stateParams, Job) {
                    return Job.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('job.new', {
            parent: 'job',
            url: '/new',
            data: {
                authorities: ['ROLE_COMPANY']
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/job/job-dialog.html',
                    controller: 'JobDialogController',
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
        .state('job.edit', {
            parent: 'job',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_COMPANY']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job/job-dialog.html',
                    controller: 'JobDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Job', function(Job) {
                            return Job.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('job.delete', {
            parent: 'job',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_COMPANY']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/job/job-delete-dialog.html',
                    controller: 'JobDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Job', function(Job) {
                            return Job.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('job', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
