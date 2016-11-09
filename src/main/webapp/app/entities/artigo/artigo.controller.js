(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ArtigoController', ArtigoController);

    ArtigoController.$inject = ['$scope', '$state', 'Artigo'];

    function ArtigoController ($scope, $state, Artigo) {
        var vm = this;
        
        vm.artigos = [];

        loadAll();

        function loadAll() {
            Artigo.query(function(result) {
                vm.artigos = result;
            });
        }
    }
})();
