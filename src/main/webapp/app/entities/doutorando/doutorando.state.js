(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('doutorando', {
            parent: 'entity',
            url: '/doutorando',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Doutorandos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/doutorando/doutorandos.html',
                    controller: 'DoutorandoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('doutorando-detail', {
            parent: 'entity',
            url: '/doutorando/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Doutorando'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/doutorando/doutorando-detail.html',
                    controller: 'DoutorandoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Doutorando', function($stateParams, Doutorando) {
                    return Doutorando.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'doutorando',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('doutorando-detail.edit', {
            parent: 'doutorando-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doutorando/doutorando-dialog.html',
                    controller: 'DoutorandoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Doutorando', function(Doutorando) {
                            return Doutorando.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('doutorando.new', {
            parent: 'doutorando',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doutorando/doutorando-dialog.html',
                    controller: 'DoutorandoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                terminou1OPeriodo: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('doutorando', null, { reload: 'doutorando' });
                }, function() {
                    $state.go('doutorando');
                });
            }]
        })
        .state('doutorando.edit', {
            parent: 'doutorando',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doutorando/doutorando-dialog.html',
                    controller: 'DoutorandoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Doutorando', function(Doutorando) {
                            return Doutorando.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('doutorando', null, { reload: 'doutorando' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('doutorando.delete', {
            parent: 'doutorando',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doutorando/doutorando-delete-dialog.html',
                    controller: 'DoutorandoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Doutorando', function(Doutorando) {
                            return Doutorando.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('doutorando', null, { reload: 'doutorando' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
