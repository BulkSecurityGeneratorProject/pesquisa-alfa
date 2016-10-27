(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('PropostaDialogController', PropostaDialogController);

    PropostaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Proposta'];

    function PropostaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Proposta) {
        var vm = this;

        vm.proposta = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.proposta.id !== null) {
                Proposta.update(vm.proposta, onSaveSuccess, onSaveError);
            } else {
                Proposta.save(vm.proposta, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:propostaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
