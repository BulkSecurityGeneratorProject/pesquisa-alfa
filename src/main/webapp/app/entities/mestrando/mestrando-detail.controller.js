(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('MestrandoDetailController', MestrandoDetailController);

    MestrandoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Mestrando', 'Aluno'];

    function MestrandoDetailController($scope, $rootScope, $stateParams, previousState, entity, Mestrando, Aluno) {
        var vm = this;

        vm.mestrando = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:mestrandoUpdate', function(event, result) {
            vm.mestrando = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
