(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC5DetailController', UC5DetailController);

    UC5DetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UC5', 'Aluno', 'Orientador'];

    function UC5DetailController($scope, $rootScope, $stateParams, previousState, entity, UC5, Aluno, Orientador) {
        var vm = this;

        vm.uc5 = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:uc5Update', function(event, result) {
            vm.uc5 = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
