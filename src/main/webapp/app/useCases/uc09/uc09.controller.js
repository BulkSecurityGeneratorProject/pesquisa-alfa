(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC09Controller', UC09Controller);

    UC09Controller.$inject = ['$scope', '$state', 'Banca'];

    function UC09Controller ($scope, $state, Banca) {
        var vm = this;

        vm.banca = [];

        loadAll();


        function loadAll() {
            Banca.query(function(result) {
                vm.banca = result;
            });
        }
    }

})();
