(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('DoutorandoDetailController', DoutorandoDetailController);

    DoutorandoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Doutorando', 'Aluno'];

    function DoutorandoDetailController($scope, $rootScope, $stateParams, previousState, entity, Doutorando, Aluno) {
        var vm = this;

        vm.doutorando = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:doutorandoUpdate', function(event, result) {
            vm.doutorando = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
