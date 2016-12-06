(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ReuniaoController', ReuniaoController);

    ReuniaoController.$inject = ['$scope', '$state', 'Reuniao'];

    function ReuniaoController ($scope, $state, Reuniao) {
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
