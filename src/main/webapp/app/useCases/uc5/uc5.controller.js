(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC5Controller', UC5Controller);

    UC5Controller.$inject = ['$scope', '$state', 'Reuniao'];

    function UC5Controller ($scope, $state, Reuniao) {
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
