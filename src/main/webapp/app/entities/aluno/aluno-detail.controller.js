(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('AlunoDetailController', AlunoDetailController);

    AlunoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Aluno', 'Usuario', 'Orientador', 'Artigo'];

    function AlunoDetailController($scope, $rootScope, $stateParams, previousState, entity, Aluno, Usuario, Orientador, Artigo) {
        var vm = this;

        vm.aluno = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:alunoUpdate', function(event, result) {
            vm.aluno = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
