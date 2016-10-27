(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('PropostaController', PropostaController);

    PropostaController.$inject = ['$scope', '$state', 'Proposta'];

    function PropostaController ($scope, $state, Proposta) {
        var vm = this;
        
        vm.propostas = [];

        loadAll();

        function loadAll() {
            Proposta.query(function(result) {
                vm.propostas = result;
            });
        }
    }
})();
