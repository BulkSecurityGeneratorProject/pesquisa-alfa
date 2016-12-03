(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('secretaria', {
            parent: 'entity',
            url: '/secretaria',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Secretarias'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/secretaria/secretarias.html',
                    controller: 'SecretariaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('secretaria-detail', {
            parent: 'entity',
            url: '/secretaria/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Secretaria'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/secretaria/secretaria-detail.html',
                    controller: 'SecretariaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Secretaria', function($stateParams, Secretaria) {
                    return Secretaria.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'secretaria',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('secretaria-detail.edit', {
            parent: 'secretaria-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/secretaria/secretaria-dialog.html',
                    controller: 'SecretariaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Secretaria', function(Secretaria) {
                            return Secretaria.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('secretaria.new', {
            parent: 'secretaria',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/secretaria/secretaria-dialog.html',
                    controller: 'SecretariaDialogController',
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
                    $state.go('secretaria', null, { reload: 'secretaria' });
                }, function() {
                    $state.go('secretaria');
                });
            }]
        })
        .state('secretaria.edit', {
            parent: 'secretaria',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/secretaria/secretaria-dialog.html',
                    controller: 'SecretariaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Secretaria', function(Secretaria) {
                            return Secretaria.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('secretaria', null, { reload: 'secretaria' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('secretaria.delete', {
            parent: 'secretaria',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/secretaria/secretaria-delete-dialog.html',
                    controller: 'SecretariaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Secretaria', function(Secretaria) {
                            return Secretaria.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('secretaria', null, { reload: 'secretaria' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
