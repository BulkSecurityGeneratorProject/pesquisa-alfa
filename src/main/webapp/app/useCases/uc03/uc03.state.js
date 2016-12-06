(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('uc03', {
            parent: 'useCases',
            url: '/uc03',
            data: {
                authorities: ['ROLE_ALUNO','ROLE_ADMIN'],
                pageTitle: 'Orientador'
            },
            views: {
                'content@': {
                    templateUrl: 'app/useCases/uc03/uc03.html',
                    controller: 'UC03Controller',
                    controllerAs: 'vm'
                }
            }
        })
        .state('uc03.new', {
            parent: 'uc03',
            url: '/new',
            data: {
                authorities: ['ROLE_ALUNO','ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/useCases/uc03/uc03-dialog.html',
                    controller: 'OrientadorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                orientador: null
                            }
                        }
                    }                    
                }).result.then(function() {
                    $state.go('uc03', null, { reload: 'uc03' });
                }, function() {
                    $state.go('uc03');
                });
            }]
        });
    }
})();
