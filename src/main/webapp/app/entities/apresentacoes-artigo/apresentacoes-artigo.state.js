(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('apresentacoes-artigo', {
            parent: 'entity',
            url: '/apresentacoes-artigo',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ApresentacoesArtigos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/apresentacoes-artigo/apresentacoes-artigos.html',
                    controller: 'ApresentacoesArtigoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('apresentacoes-artigo-detail', {
            parent: 'entity',
            url: '/apresentacoes-artigo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ApresentacoesArtigo'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/apresentacoes-artigo/apresentacoes-artigo-detail.html',
                    controller: 'ApresentacoesArtigoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ApresentacoesArtigo', function($stateParams, ApresentacoesArtigo) {
                    return ApresentacoesArtigo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'apresentacoes-artigo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('apresentacoes-artigo-detail.edit', {
            parent: 'apresentacoes-artigo-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apresentacoes-artigo/apresentacoes-artigo-dialog.html',
                    controller: 'ApresentacoesArtigoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApresentacoesArtigo', function(ApresentacoesArtigo) {
                            return ApresentacoesArtigo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('apresentacoes-artigo.new', {
            parent: 'apresentacoes-artigo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apresentacoes-artigo/apresentacoes-artigo-dialog.html',
                    controller: 'ApresentacoesArtigoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dataApresentacao: null,
                                local: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('apresentacoes-artigo', null, { reload: 'apresentacoes-artigo' });
                }, function() {
                    $state.go('apresentacoes-artigo');
                });
            }]
        })
        .state('apresentacoes-artigo.edit', {
            parent: 'apresentacoes-artigo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apresentacoes-artigo/apresentacoes-artigo-dialog.html',
                    controller: 'ApresentacoesArtigoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ApresentacoesArtigo', function(ApresentacoesArtigo) {
                            return ApresentacoesArtigo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('apresentacoes-artigo', null, { reload: 'apresentacoes-artigo' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('apresentacoes-artigo.delete', {
            parent: 'apresentacoes-artigo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/apresentacoes-artigo/apresentacoes-artigo-delete-dialog.html',
                    controller: 'ApresentacoesArtigoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ApresentacoesArtigo', function(ApresentacoesArtigo) {
                            return ApresentacoesArtigo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('apresentacoes-artigo', null, { reload: 'apresentacoes-artigo' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
