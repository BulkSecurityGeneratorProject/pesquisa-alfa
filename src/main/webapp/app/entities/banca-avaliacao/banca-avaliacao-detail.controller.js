(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('BancaAvaliacaoDetailController', BancaAvaliacaoDetailController);

    BancaAvaliacaoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BancaAvaliacao', 'Proposta', 'Professor'];

    function BancaAvaliacaoDetailController($scope, $rootScope, $stateParams, previousState, entity, BancaAvaliacao, Proposta, Professor) {
        var vm = this;

        vm.bancaAvaliacao = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:bancaAvaliacaoUpdate', function(event, result) {
            vm.bancaAvaliacao = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
