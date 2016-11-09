(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('PropostaDialogController', PropostaDialogController);

    PropostaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Proposta', 'Aluno', 'Orientador'];

    function PropostaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Proposta, Aluno, Orientador) {
        var vm = this;

        vm.proposta = entity;
        vm.clear = clear;
        vm.save = save;
        vm.alunos = Aluno.query({filter: 'proposta-is-null'});
        $q.all([vm.proposta.$promise, vm.alunos.$promise]).then(function() {
            if (!vm.proposta.aluno || !vm.proposta.aluno.id) {
                return $q.reject();
            }
            return Aluno.get({id : vm.proposta.aluno.id}).$promise;
        }).then(function(aluno) {
            vm.alunos.push(aluno);
        });
        vm.orientadors = Orientador.query();

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
