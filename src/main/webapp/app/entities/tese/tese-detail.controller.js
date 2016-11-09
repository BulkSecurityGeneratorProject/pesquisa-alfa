(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('TeseDetailController', TeseDetailController);

    TeseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tese', 'Proposta'];

    function TeseDetailController($scope, $rootScope, $stateParams, previousState, entity, Tese, Proposta) {
        var vm = this;

        vm.tese = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:teseUpdate', function(event, result) {
            vm.tese = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
