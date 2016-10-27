(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('gerente', {
            parent: 'entity',
            url: '/gerente',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Gerentes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gerente/gerentes.html',
                    controller: 'GerenteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('gerente-detail', {
            parent: 'entity',
            url: '/gerente/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Gerente'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gerente/gerente-detail.html',
                    controller: 'GerenteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Gerente', function($stateParams, Gerente) {
                    return Gerente.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'gerente',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('gerente-detail.edit', {
            parent: 'gerente-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gerente/gerente-dialog.html',
                    controller: 'GerenteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Gerente', function(Gerente) {
                            return Gerente.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gerente.new', {
            parent: 'gerente',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gerente/gerente-dialog.html',
                    controller: 'GerenteDialogController',
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
                    $state.go('gerente', null, { reload: 'gerente' });
                }, function() {
                    $state.go('gerente');
                });
            }]
        })
        .state('gerente.edit', {
            parent: 'gerente',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gerente/gerente-dialog.html',
                    controller: 'GerenteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Gerente', function(Gerente) {
                            return Gerente.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gerente', null, { reload: 'gerente' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gerente.delete', {
            parent: 'gerente',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gerente/gerente-delete-dialog.html',
                    controller: 'GerenteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Gerente', function(Gerente) {
                            return Gerente.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gerente', null, { reload: 'gerente' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
