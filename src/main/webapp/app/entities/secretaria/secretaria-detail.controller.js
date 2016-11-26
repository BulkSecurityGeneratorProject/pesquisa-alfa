(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('SecretariaDetailController', SecretariaDetailController);

    SecretariaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Secretaria', 'Usuario'];

    function SecretariaDetailController($scope, $rootScope, $stateParams, previousState, entity, Secretaria, Usuario) {
        var vm = this;

        vm.secretaria = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pesquisaalfaApp:secretariaUpdate', function(event, result) {
            vm.secretaria = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
