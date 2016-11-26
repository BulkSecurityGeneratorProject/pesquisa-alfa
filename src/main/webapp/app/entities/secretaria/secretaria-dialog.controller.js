(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('SecretariaDialogController', SecretariaDialogController);

    SecretariaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Secretaria', 'Usuario'];

    function SecretariaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Secretaria, Usuario) {
        var vm = this;

        vm.secretaria = entity;
        vm.clear = clear;
        vm.save = save;
        vm.usuarios = Usuario.query({filter: 'secretaria-is-null'});
        $q.all([vm.secretaria.$promise, vm.usuarios.$promise]).then(function() {
            if (!vm.secretaria.usuario || !vm.secretaria.usuario.id) {
                return $q.reject();
            }
            return Usuario.get({id : vm.secretaria.usuario.id}).$promise;
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
            if (vm.secretaria.id !== null) {
                Secretaria.update(vm.secretaria, onSaveSuccess, onSaveError);
            } else {
                Secretaria.save(vm.secretaria, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:secretariaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
