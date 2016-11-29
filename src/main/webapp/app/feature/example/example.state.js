(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('example', {
            parent: 'feature',
            url: '/example',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'global.menu.feature.example'
            },
            views: {
                'content@': {
                    templateUrl: 'app/feature/example/example.html',
                    controller: 'ExampleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('example');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
