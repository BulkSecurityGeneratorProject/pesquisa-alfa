(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ApresentacoesArtigoDialogController', ApresentacoesArtigoDialogController);

    ApresentacoesArtigoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ApresentacoesArtigo', 'Artigo'];

    function ApresentacoesArtigoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ApresentacoesArtigo, Artigo) {
        var vm = this;

        vm.apresentacoesArtigo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.artigos = Artigo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.apresentacoesArtigo.id !== null) {
                ApresentacoesArtigo.update(vm.apresentacoesArtigo, onSaveSuccess, onSaveError);
            } else {
                ApresentacoesArtigo.save(vm.apresentacoesArtigo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:apresentacoesArtigoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dataApresentacao = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
