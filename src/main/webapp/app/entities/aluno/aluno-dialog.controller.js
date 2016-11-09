(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('AlunoDialogController', AlunoDialogController);

    AlunoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Aluno', 'Usuario', 'Orientador', 'Artigo'];

    function AlunoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Aluno, Usuario, Orientador, Artigo) {
        var vm = this;

        vm.aluno = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.usuarios = Usuario.query({filter: 'aluno-is-null'});
        $q.all([vm.aluno.$promise, vm.usuarios.$promise]).then(function() {
            if (!vm.aluno.usuario || !vm.aluno.usuario.id) {
                return $q.reject();
            }
            return Usuario.get({id : vm.aluno.usuario.id}).$promise;
        }).then(function(usuario) {
            vm.usuarios.push(usuario);
        });
        vm.orientadors = Orientador.query();
        vm.artigos = Artigo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.aluno.id !== null) {
                Aluno.update(vm.aluno, onSaveSuccess, onSaveError);
            } else {
                Aluno.save(vm.aluno, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:alunoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dataMatricula = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
