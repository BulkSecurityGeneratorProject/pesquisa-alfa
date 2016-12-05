(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('uc06', {
            parent: 'useCases',
            url: '/uc06',
            data: {
                authorities: ['ROLE_ALUNO','ROLE_ADMIN'],
                pageTitle: 'Artigos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/useCases/uc06/uc06.html',
                    controller: 'UC06Controller',
                    controllerAs: 'vm'
                }
            }
        })
        .state('uc06.new', {
            parent: 'uc06',
            url: '/new',
            data: {
                authorities: ['ROLE_ALUNO','ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/useCases/uc06/uc06-dialog.html',
                    controller: 'ArtigoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                titulo: null,
                                resumo: null,
                                id: null,
                                aceito: null,
                                apresentacoes: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('uc06', null, { reload: 'uc06' });
                }, function() {
                    $state.go('uc06');
                });
            }]
        });
    }
})();
