(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('GerenteDialogController', GerenteDialogController);

    GerenteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Gerente', 'Usuario'];

    function GerenteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Gerente, Usuario) {
        var vm = this;

        vm.gerente = entity;
        vm.clear = clear;
        vm.save = save;
        vm.usuarios = Usuario.query({filter: 'gerente-is-null'});
        $q.all([vm.gerente.$promise, vm.usuarios.$promise]).then(function() {
            if (!vm.gerente.usuario || !vm.gerente.usuario.id) {
                return $q.reject();
            }
            return Usuario.get({id : vm.gerente.usuario.id}).$promise;
        }).then(function(usuario) {
            vm.usuarios.push(usuario);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.gerente.id !== null) {
                Gerente.update(vm.gerente, onSaveSuccess, onSaveError);
            } else {
                Gerente.save(vm.gerente, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:gerenteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
