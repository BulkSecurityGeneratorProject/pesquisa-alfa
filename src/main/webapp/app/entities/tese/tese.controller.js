(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('TeseController', TeseController);

    TeseController.$inject = ['$scope', '$state', 'Tese'];

    function TeseController ($scope, $state, Tese) {
        var vm = this;
        
        vm.tese = [];

        loadAll();

        function loadAll() {
            Tese.query(function(result) {
                vm.tese = result;
            });
        }
    }
})();
