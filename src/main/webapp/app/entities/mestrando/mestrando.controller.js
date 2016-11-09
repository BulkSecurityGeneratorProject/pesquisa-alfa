(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('MestrandoController', MestrandoController);

    MestrandoController.$inject = ['$scope', '$state', 'Mestrando'];

    function MestrandoController ($scope, $state, Mestrando) {
        var vm = this;
        
        vm.mestrandos = [];

        loadAll();

        function loadAll() {
            Mestrando.query(function(result) {
                vm.mestrandos = result;
            });
        }
    }
})();
