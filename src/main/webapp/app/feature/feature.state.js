(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('feature', {
            abstract: true,
            parent: 'app'
        });
    }
})();
