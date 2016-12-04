(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('uc11', {
            parent: 'useCases',
            url: '/uc11',
            data: {
                authorities: ['ROLE_PROFESSOR','ROLE_ADMIN'],
                pageTitle: 'Gerenciar Reuniões'
            },
            views: {
                'content@': {
                    templateUrl: 'app/useCases/uc11/uc11.html',
                    controller: 'UC11Controller',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('uc11-detail', {
            parent: 'useCases',
            url: '/uc11/{id}',
            data: {
                authorities: ['ROLE_PROFESSOR','ROLE_ADMIN'],
                pageTitle: 'Detalhes das reuniões'
            },
            views: {
                'content@': {
                    templateUrl: 'app/useCases/uc11/uc11-detail.html',
                    controller: 'ReuniaoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Reuniao', function($stateParams, Reuniao) {
                    return Reuniao.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'uc11',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('uc11-detail.edit', {
            parent: 'uc11-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_PROFESSOR','ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/useCases/uc11/uc11-dialog.html',
                    controller: 'ReuniaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Reuniao', function(Reuniao) {
                            return Reuniao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })/*
        .state('uc11.new', {
            parent: 'uc11',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/useCases/uc11/uc11-dialog.html',
                    controller: 'ReuniaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                horario: null,
                                assunto: null,
                                reuniaoAprovada: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('uc11', null, { reload: 'uc11' });
                }, function() {
                    $state.go('uc11');
                });
            }]
        })*/
        .state('uc11.edit', {
            parent: 'uc11',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_PROFESSOR','ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/useCases/uc11/uc11-dialog.html',
                    controller: 'ReuniaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Reuniao', function(Reuniao) {
                            return Reuniao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('uc11', null, { reload: 'uc11' });
                }, function() {
                    $state.go('^');
                });
            }]
        })/*
        .state('reuniao.delete', {
            parent: 'reuniao',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reuniao/reuniao-delete-dialog.html',
                    controller: 'ReuniaoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Reuniao', function(Reuniao) {
                            return Reuniao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reuniao', null, { reload: 'reuniao' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        */
        ;
    }

})();
