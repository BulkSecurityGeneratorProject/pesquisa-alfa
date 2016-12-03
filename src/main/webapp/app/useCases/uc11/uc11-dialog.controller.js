(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC11DialogController', UC11DialogController);

    UC11DialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UC11', 'Aluno', 'Orientador'];

    function UC11DialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, UC11, Aluno, Orientador) {
        var vm = this;

        vm.uc5 = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.alunos = Aluno.query({filter: 'uc5-is-null'});
        $q.all([vm.uc5.$promise, vm.alunos.$promise]).then(function() {
            if (!vm.uc5.aluno || !vm.uc5.aluno.id) {
                return $q.reject();
            }
            return Aluno.get({id : vm.uc5.aluno.id}).$promise;
        }).then(function(aluno) {
            vm.alunos.push(aluno);
        });
        vm.orientadors = Orientador.query({filter: 'uc5-is-null'});
        $q.all([vm.uc5.$promise, vm.orientadors.$promise]).then(function() {
            if (!vm.uc5.orientador || !vm.uc5.orientador.id) {
                return $q.reject();
            }
            return Orientador.get({id : vm.uc5.orientador.id}).$promise;
        }).then(function(orientador) {
            vm.orientadors.push(orientador);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.uc5.id !== null) {
                UC11.update(vm.uc5, onSaveSuccess, onSaveError);
            } else {
                UC11.save(vm.uc5, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:uc5Update', result);
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
