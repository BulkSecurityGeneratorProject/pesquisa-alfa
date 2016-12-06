(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ApresentacoesArtigoController', ApresentacoesArtigoController);

    ApresentacoesArtigoController.$inject = ['$scope', '$state', 'ApresentacoesArtigo'];

    function ApresentacoesArtigoController ($scope, $state, ApresentacoesArtigo) {
        var vm = this;
        
        vm.apresentacoesArtigos = [];

        loadAll();

        function loadAll() {
            ApresentacoesArtigo.query(function(result) {
                vm.apresentacoesArtigos = result;
            });
        }
    }
})();
