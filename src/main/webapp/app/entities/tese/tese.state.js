(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tese', {
            parent: 'entity',
            url: '/tese',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tese'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tese/tese.html',
                    controller: 'TeseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('tese-detail', {
            parent: 'entity',
            url: '/tese/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tese'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tese/tese-detail.html',
                    controller: 'TeseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Tese', function($stateParams, Tese) {
                    return Tese.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tese',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tese-detail.edit', {
            parent: 'tese-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tese/tese-dialog.html',
                    controller: 'TeseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tese', function(Tese) {
                            return Tese.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tese.new', {
            parent: 'tese',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tese/tese-dialog.html',
                    controller: 'TeseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                conceitoMedioPesquisa: null,
                                teseAprovada: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tese', null, { reload: 'tese' });
                }, function() {
                    $state.go('tese');
                });
            }]
        })
        .state('tese.edit', {
            parent: 'tese',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tese/tese-dialog.html',
                    controller: 'TeseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tese', function(Tese) {
                            return Tese.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tese', null, { reload: 'tese' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tese.delete', {
            parent: 'tese',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tese/tese-delete-dialog.html',
                    controller: 'TeseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tese', function(Tese) {
                            return Tese.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tese', null, { reload: 'tese' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
