(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('uc09', {
            parent: 'useCases',
            url: '/uc09',
            data: {
                authorities: ['ROLE_PROFESSOR','ROLE_ADMIN'],
                pageTitle: 'Gerenciar Bancas Avaliadoras'
            },
            views: {
                'content@': {
                    templateUrl: 'app/useCases/uc09/uc09.html',
                    controller: 'UC09Controller',
                    controllerAs: 'vm'
                }
            }
        })
        .state('uc09.new', {
            parent: 'uc09',
            url: '/new',
            data: {
                authorities: ['ROLE_PROFESSOR','ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/useCases/uc09/uc09-dialog.html',
                    controller: 'UC09Controller',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null,
                                aluno: null,
                                banca: null,
                                horario: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('uc09', null, { reload: 'uc09' });
                }, function() {
                    $state.go('uc09');
                });
            }]
        });
    }
})();
