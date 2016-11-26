(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('banca-avaliacao', {
            parent: 'entity',
            url: '/banca-avaliacao',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pesquisaalfaApp.bancaAvaliacao.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/banca-avaliacao/banca-avaliacaos.html',
                    controller: 'BancaAvaliacaoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bancaAvaliacao');
                    $translatePartialLoader.addPart('tiposAvaliacao');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('banca-avaliacao-detail', {
            parent: 'entity',
            url: '/banca-avaliacao/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pesquisaalfaApp.bancaAvaliacao.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/banca-avaliacao/banca-avaliacao-detail.html',
                    controller: 'BancaAvaliacaoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bancaAvaliacao');
                    $translatePartialLoader.addPart('tiposAvaliacao');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BancaAvaliacao', function($stateParams, BancaAvaliacao) {
                    return BancaAvaliacao.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'banca-avaliacao',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('banca-avaliacao-detail.edit', {
            parent: 'banca-avaliacao-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banca-avaliacao/banca-avaliacao-dialog.html',
                    controller: 'BancaAvaliacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BancaAvaliacao', function(BancaAvaliacao) {
                            return BancaAvaliacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('banca-avaliacao.new', {
            parent: 'banca-avaliacao',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banca-avaliacao/banca-avaliacao-dialog.html',
                    controller: 'BancaAvaliacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dataHoraApresentacao: null,
                                tipoAvaliacao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('banca-avaliacao', null, { reload: 'banca-avaliacao' });
                }, function() {
                    $state.go('banca-avaliacao');
                });
            }]
        })
        .state('banca-avaliacao.edit', {
            parent: 'banca-avaliacao',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banca-avaliacao/banca-avaliacao-dialog.html',
                    controller: 'BancaAvaliacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BancaAvaliacao', function(BancaAvaliacao) {
                            return BancaAvaliacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('banca-avaliacao', null, { reload: 'banca-avaliacao' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('banca-avaliacao.delete', {
            parent: 'banca-avaliacao',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banca-avaliacao/banca-avaliacao-delete-dialog.html',
                    controller: 'BancaAvaliacaoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BancaAvaliacao', function(BancaAvaliacao) {
                            return BancaAvaliacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('banca-avaliacao', null, { reload: 'banca-avaliacao' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
