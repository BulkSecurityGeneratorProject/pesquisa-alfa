(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC10Controller', UC10Controller);

    UC10Controller.$inject = ['$scope', '$state', 'Proposta'];

    function UC10Controller ($scope, $state, Proposta) {
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
