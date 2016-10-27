(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('GerenteController', GerenteController);

    GerenteController.$inject = ['$scope', '$state', 'Gerente'];

    function GerenteController ($scope, $state, Gerente) {
        var vm = this;
        
        vm.gerentes = [];

        loadAll();

        function loadAll() {
            Gerente.query(function(result) {
                vm.gerentes = result;
            });
        }
    }
})();
