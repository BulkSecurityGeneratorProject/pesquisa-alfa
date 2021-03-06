(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reuniao', {
            parent: 'entity',
            url: '/reuniao',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Reuniaos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reuniao/reuniaos.html',
                    controller: 'ReuniaoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('reuniao-detail', {
            parent: 'entity',
            url: '/reuniao/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Reuniao'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reuniao/reuniao-detail.html',
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
                        name: $state.current.name || 'reuniao',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('reuniao-detail.edit', {
            parent: 'reuniao-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reuniao/reuniao-dialog.html',
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
        })
        .state('reuniao.new', {
            parent: 'reuniao',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reuniao/reuniao-dialog.html',
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
                    $state.go('reuniao', null, { reload: 'reuniao' });
                }, function() {
                    $state.go('reuniao');
                });
            }]
        })
        .state('reuniao.edit', {
            parent: 'reuniao',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reuniao/reuniao-dialog.html',
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
                    $state.go('reuniao', null, { reload: 'reuniao' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
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
        });
    }

})();
