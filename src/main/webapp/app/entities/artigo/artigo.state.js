(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('artigo', {
            parent: 'entity',
            url: '/artigo',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Artigos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/artigo/artigos.html',
                    controller: 'ArtigoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('artigo-detail', {
            parent: 'entity',
            url: '/artigo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Artigo'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/artigo/artigo-detail.html',
                    controller: 'ArtigoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Artigo', function($stateParams, Artigo) {
                    return Artigo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'artigo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('artigo-detail.edit', {
            parent: 'artigo-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artigo/artigo-dialog.html',
                    controller: 'ArtigoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Artigo', function(Artigo) {
                            return Artigo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('artigo.new', {
            parent: 'artigo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artigo/artigo-dialog.html',
                    controller: 'ArtigoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                titulo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('artigo', null, { reload: 'artigo' });
                }, function() {
                    $state.go('artigo');
                });
            }]
        })
        .state('artigo.edit', {
            parent: 'artigo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artigo/artigo-dialog.html',
                    controller: 'ArtigoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Artigo', function(Artigo) {
                            return Artigo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('artigo', null, { reload: 'artigo' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('artigo.delete', {
            parent: 'artigo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/artigo/artigo-delete-dialog.html',
                    controller: 'ArtigoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Artigo', function(Artigo) {
                            return Artigo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('artigo', null, { reload: 'artigo' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
