(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC06Controller', UC06Controller);

    UC06Controller.$inject = ['$scope', '$state', 'Artigo'];

    function UC06Controller ($scope, $state, Artigo) {
        var vm = this;

        vm.artigos = [];

        loadAll();

        function loadAll() {
            Artigo.query(function(result) {
                vm.artigo = result;
            });
        }
    }

})();
