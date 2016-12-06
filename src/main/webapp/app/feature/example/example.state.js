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
                pageTitle: 'Example'
            },
            views: {
                'content@': {
                    templateUrl: 'app/feature/example/example.html',
                    controller: 'ExampleController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();
