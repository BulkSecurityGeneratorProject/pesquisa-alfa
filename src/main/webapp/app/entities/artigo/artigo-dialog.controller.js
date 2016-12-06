(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ArtigoDialogController', ArtigoDialogController);

    ArtigoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Artigo', 'Aluno'];

    function ArtigoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Artigo, Aluno) {
        var vm = this;

        vm.artigo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.alunos = Aluno.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.artigo.id !== null) {
                Artigo.update(vm.artigo, onSaveSuccess, onSaveError);
            } else {
                Artigo.save(vm.artigo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:artigoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
