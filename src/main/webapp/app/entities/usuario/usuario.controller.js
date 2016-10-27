(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UsuarioController', UsuarioController);

    UsuarioController.$inject = ['$scope', '$state', 'Usuario'];

    function UsuarioController ($scope, $state, Usuario) {
        var vm = this;
        
        vm.usuarios = [];

        loadAll();

        function loadAll() {
            Usuario.query(function(result) {
                vm.usuarios = result;
            });
        }
    }
})();
