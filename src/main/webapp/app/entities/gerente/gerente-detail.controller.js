(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('GerenteDetailController', GerenteDetailController);

    GerenteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Gerente', 'Usuario'];

    function GerenteDetailController($scope, $rootScope, $stateParams, previousState, entity, Gerente, Usuario) {
        var vm = this;

        vm.gerente = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:gerenteUpdate', function(event, result) {
            vm.gerente = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
