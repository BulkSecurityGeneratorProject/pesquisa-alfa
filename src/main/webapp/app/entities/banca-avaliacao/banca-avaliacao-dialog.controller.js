(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('BancaAvaliacaoDialogController', BancaAvaliacaoDialogController);

    BancaAvaliacaoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BancaAvaliacao', 'Professor'];

    function BancaAvaliacaoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BancaAvaliacao, Professor) {
        var vm = this;

        vm.bancaAvaliacao = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.professors = Professor.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bancaAvaliacao.id !== null) {
                BancaAvaliacao.update(vm.bancaAvaliacao, onSaveSuccess, onSaveError);
            } else {
                BancaAvaliacao.save(vm.bancaAvaliacao, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:bancaAvaliacaoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dataHoraApresentacao = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
