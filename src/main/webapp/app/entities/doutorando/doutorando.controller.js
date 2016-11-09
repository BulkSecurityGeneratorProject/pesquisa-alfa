(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('DoutorandoController', DoutorandoController);

    DoutorandoController.$inject = ['$scope', '$state', 'Doutorando'];

    function DoutorandoController ($scope, $state, Doutorando) {
        var vm = this;
        
        vm.doutorandos = [];

        loadAll();

        function loadAll() {
            Doutorando.query(function(result) {
                vm.doutorandos = result;
            });
        }
    }
})();
