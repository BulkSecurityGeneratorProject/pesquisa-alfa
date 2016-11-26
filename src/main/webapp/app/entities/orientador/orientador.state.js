(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('orientador', {
            parent: 'entity',
            url: '/orientador',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pesquisaalfaApp.orientador.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/orientador/orientadors.html',
                    controller: 'OrientadorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('orientador');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('orientador-detail', {
            parent: 'entity',
            url: '/orientador/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pesquisaalfaApp.orientador.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/orientador/orientador-detail.html',
                    controller: 'OrientadorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('orientador');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Orientador', function($stateParams, Orientador) {
                    return Orientador.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'orientador',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('orientador-detail.edit', {
            parent: 'orientador-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/orientador/orientador-dialog.html',
                    controller: 'OrientadorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Orientador', function(Orientador) {
                            return Orientador.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('orientador.new', {
            parent: 'orientador',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/orientador/orientador-dialog.html',
                    controller: 'OrientadorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('orientador', null, { reload: 'orientador' });
                }, function() {
                    $state.go('orientador');
                });
            }]
        })
        .state('orientador.edit', {
            parent: 'orientador',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/orientador/orientador-dialog.html',
                    controller: 'OrientadorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Orientador', function(Orientador) {
                            return Orientador.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('orientador', null, { reload: 'orientador' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('orientador.delete', {
            parent: 'orientador',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/orientador/orientador-delete-dialog.html',
                    controller: 'OrientadorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Orientador', function(Orientador) {
                            return Orientador.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('orientador', null, { reload: 'orientador' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
