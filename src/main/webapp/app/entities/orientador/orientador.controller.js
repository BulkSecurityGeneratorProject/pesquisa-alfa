(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('OrientadorController', OrientadorController);

    OrientadorController.$inject = ['$scope', '$state', 'Orientador'];

    function OrientadorController ($scope, $state, Orientador) {
        var vm = this;
        
        vm.orientadors = [];

        loadAll();

        function loadAll() {
            Orientador.query(function(result) {
                vm.orientadors = result;
            });
        }
    }
})();
