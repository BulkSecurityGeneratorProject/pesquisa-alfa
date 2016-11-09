(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('OrientadorDetailController', OrientadorDetailController);

    OrientadorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Orientador', 'Professor'];

    function OrientadorDetailController($scope, $rootScope, $stateParams, previousState, entity, Orientador, Professor) {
        var vm = this;

        vm.orientador = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:orientadorUpdate', function(event, result) {
            vm.orientador = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
