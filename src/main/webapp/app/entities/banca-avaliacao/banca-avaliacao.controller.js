(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('BancaAvaliacaoController', BancaAvaliacaoController);

    BancaAvaliacaoController.$inject = ['$scope', '$state', 'BancaAvaliacao'];

    function BancaAvaliacaoController ($scope, $state, BancaAvaliacao) {
        var vm = this;
        
        vm.bancaAvaliacaos = [];

        loadAll();

        function loadAll() {
            BancaAvaliacao.query(function(result) {
                vm.bancaAvaliacaos = result;
            });
        }
    }
})();
