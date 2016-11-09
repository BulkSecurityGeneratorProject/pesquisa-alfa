(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ProfessorDetailController', ProfessorDetailController);

    ProfessorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Professor', 'Usuario', 'BancaAvaliacao'];

    function ProfessorDetailController($scope, $rootScope, $stateParams, previousState, entity, Professor, Usuario, BancaAvaliacao) {
        var vm = this;

        vm.professor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:professorUpdate', function(event, result) {
            vm.professor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
