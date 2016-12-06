(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ApresentacoesArtigoDetailController', ApresentacoesArtigoDetailController);

    ApresentacoesArtigoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ApresentacoesArtigo', 'Artigo'];

    function ApresentacoesArtigoDetailController($scope, $rootScope, $stateParams, previousState, entity, ApresentacoesArtigo, Artigo) {
        var vm = this;

        vm.apresentacoesArtigo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:apresentacoesArtigoUpdate', function(event, result) {
            vm.apresentacoesArtigo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
