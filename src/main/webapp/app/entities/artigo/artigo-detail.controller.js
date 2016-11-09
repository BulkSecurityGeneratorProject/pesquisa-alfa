(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ArtigoDetailController', ArtigoDetailController);

    ArtigoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Artigo', 'Aluno'];

    function ArtigoDetailController($scope, $rootScope, $stateParams, previousState, entity, Artigo, Aluno) {
        var vm = this;

        vm.artigo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:artigoUpdate', function(event, result) {
            vm.artigo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
