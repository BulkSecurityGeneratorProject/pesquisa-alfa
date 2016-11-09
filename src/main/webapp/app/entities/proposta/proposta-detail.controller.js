(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('PropostaDetailController', PropostaDetailController);

    PropostaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Proposta', 'Aluno', 'Orientador'];

    function PropostaDetailController($scope, $rootScope, $stateParams, previousState, entity, Proposta, Aluno, Orientador) {
        var vm = this;

        vm.proposta = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:propostaUpdate', function(event, result) {
            vm.proposta = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
