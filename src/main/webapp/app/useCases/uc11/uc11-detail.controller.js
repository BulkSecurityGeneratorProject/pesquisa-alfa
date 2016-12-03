(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC11DetailController', UC11DetailController);

    UC11DetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UC11', 'Aluno', 'Orientador'];

    function UC11DetailController($scope, $rootScope, $stateParams, previousState, entity, UC11, Aluno, Orientador) {
        var vm = this;

        vm.uc5 = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:uc5Update', function(event, result) {
            vm.uc5 = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
