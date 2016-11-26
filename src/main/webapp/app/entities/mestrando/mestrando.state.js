(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('mestrando', {
            parent: 'entity',
            url: '/mestrando',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pesquisaalfaApp.mestrando.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mestrando/mestrandos.html',
                    controller: 'MestrandoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('mestrando');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('mestrando-detail', {
            parent: 'entity',
            url: '/mestrando/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pesquisaalfaApp.mestrando.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/mestrando/mestrando-detail.html',
                    controller: 'MestrandoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('mestrando');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Mestrando', function($stateParams, Mestrando) {
                    return Mestrando.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'mestrando',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('mestrando-detail.edit', {
            parent: 'mestrando-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mestrando/mestrando-dialog.html',
                    controller: 'MestrandoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Mestrando', function(Mestrando) {
                            return Mestrando.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mestrando.new', {
            parent: 'mestrando',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mestrando/mestrando-dialog.html',
                    controller: 'MestrandoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                terminouObrigatorias: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('mestrando', null, { reload: 'mestrando' });
                }, function() {
                    $state.go('mestrando');
                });
            }]
        })
        .state('mestrando.edit', {
            parent: 'mestrando',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mestrando/mestrando-dialog.html',
                    controller: 'MestrandoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Mestrando', function(Mestrando) {
                            return Mestrando.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mestrando', null, { reload: 'mestrando' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('mestrando.delete', {
            parent: 'mestrando',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/mestrando/mestrando-delete-dialog.html',
                    controller: 'MestrandoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Mestrando', function(Mestrando) {
                            return Mestrando.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('mestrando', null, { reload: 'mestrando' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
