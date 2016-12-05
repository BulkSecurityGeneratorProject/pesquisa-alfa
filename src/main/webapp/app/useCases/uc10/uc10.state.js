(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('uc10', {
            parent: 'useCases',
            url: '/uc10',
            data: {
                authorities: ['ROLE_SECRETARIA','ROLE_ADMIN'],
                pageTitle: 'Criar ou alterar propostas'
            },
            views: {
                'content@': {
                    templateUrl: 'app/useCases/uc10/uc10.html',
                    controller: 'UC10Controller',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
/*        .state('proposta-detail', {
            parent: 'entity',
            url: '/proposta/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Proposta'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/proposta/proposta-detail.html',
                    controller: 'PropostaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Proposta', function($stateParams, Proposta) {
                    return Proposta.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'proposta',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })*/
        .state('uc10.new', {
            parent: 'uc10',
            url: '/new',
            data: {
                authorities: ['ROLE_SECRETARIA','ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/useCases/uc10/uc10-dialog.html',
                    controller: 'PropostaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                horario: null,
                                assunto: null,
                                propostaAprovada: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('uc10', null, { reload: 'uc10' });
                }, function() {
                    $state.go('uc10');
                });
            }]
        })/*
        .state('proposta.edit', {
            parent: 'proposta',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/proposta/proposta-dialog.html',
                    controller: 'PropostaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Proposta', function(Proposta) {
                            return Proposta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('proposta', null, { reload: 'proposta' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('proposta.delete', {
            parent: 'proposta',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/proposta/proposta-delete-dialog.html',
                    controller: 'PropostaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Proposta', function(Proposta) {
                            return Proposta.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('proposta', null, { reload: 'proposta' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        */
        ;
    }

})();
