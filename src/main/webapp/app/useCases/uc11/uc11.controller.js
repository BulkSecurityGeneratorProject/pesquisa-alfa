(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC11Controller', UC11Controller);

    UC11Controller.$inject = ['$scope', '$state', 'Reuniao'];

    function UC11Controller ($scope, $state, Reuniao) {
        var vm = this;
        
        vm.reuniaos = [];

        loadAll();

        function loadAll() {
            Reuniao.query(function(result) {
                vm.reuniaos = result;
            });
        }
    }
})();
