(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('SecretariaController', SecretariaController);

    SecretariaController.$inject = ['$scope', '$state', 'Secretaria'];

    function SecretariaController ($scope, $state, Secretaria) {
        var vm = this;
        
        vm.secretarias = [];

        loadAll();

        function loadAll() {
            Secretaria.query(function(result) {
                vm.secretarias = result;
            });
        }
    }
})();
