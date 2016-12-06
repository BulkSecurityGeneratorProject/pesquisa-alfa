(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC03Controller', UC03Controller);

    UC03Controller.$inject = ['$scope', '$state', 'Orientador'];

    function UC03Controller ($scope, $state, Orientador) {
        var vm = this;

        vm.orientador = [];
    }

})();
