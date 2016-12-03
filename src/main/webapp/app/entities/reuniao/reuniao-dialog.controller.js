(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ReuniaoDialogController', ReuniaoDialogController);

    ReuniaoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Reuniao', 'Aluno', 'Orientador'];

    function ReuniaoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Reuniao, Aluno, Orientador) {
        var vm = this;

        vm.reuniao = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.alunos = Aluno.query();
        vm.orientadors = Orientador.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.reuniao.id !== null) {
                Reuniao.update(vm.reuniao, onSaveSuccess, onSaveError);
            } else {
                Reuniao.save(vm.reuniao, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:reuniaoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.horario = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
