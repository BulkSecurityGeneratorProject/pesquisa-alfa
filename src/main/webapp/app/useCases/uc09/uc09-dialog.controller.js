(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC09DialogController', UC09DialogController);

    UC09DialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UC09', 'Aluno', 'Orientador'];

    function UC09DialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, UC09, Aluno, Orientador) {
        var vm = this;

        vm.uc09 = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.alunos = Aluno.query({filter: 'uc09-is-null'});
        $q.all([vm.uc09.$promise, vm.alunos.$promise]).then(function() {
            if (!vm.uc09.aluno || !vm.uc09.aluno.id) {
                return $q.reject();
            }
            return Aluno.get({id : vm.uc09.aluno.id}).$promise;
        }).then(function(aluno) {
            vm.alunos.push(aluno);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.uc09.id !== null) {
                uc09.update(vm.uc09, onSaveSuccess, onSaveError);
            } else {
                UC09.save(vm.uc09, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:uc09Update', result);
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
