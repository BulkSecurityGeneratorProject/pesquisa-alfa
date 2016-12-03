(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ReuniaoDetailController', ReuniaoDetailController);

    ReuniaoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Reuniao', 'Aluno', 'Orientador'];

    function ReuniaoDetailController($scope, $rootScope, $stateParams, previousState, entity, Reuniao, Aluno, Orientador) {
        var vm = this;

        vm.reuniao = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:reuniaoUpdate', function(event, result) {
            vm.reuniao = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
