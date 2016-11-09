(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ProfessorDialogController', ProfessorDialogController);

    ProfessorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Professor', 'Usuario', 'BancaAvaliacao'];

    function ProfessorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Professor, Usuario, BancaAvaliacao) {
        var vm = this;

        vm.professor = entity;
        vm.clear = clear;
        vm.save = save;
        vm.usuarios = Usuario.query({filter: 'professor-is-null'});
        $q.all([vm.professor.$promise, vm.usuarios.$promise]).then(function() {
            if (!vm.professor.usuario || !vm.professor.usuario.id) {
                return $q.reject();
            }
            return Usuario.get({id : vm.professor.usuario.id}).$promise;
        }).then(function(usuario) {
            vm.usuarios.push(usuario);
        });
        vm.bancaavaliacaos = BancaAvaliacao.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.professor.id !== null) {
                Professor.update(vm.professor, onSaveSuccess, onSaveError);
            } else {
                Professor.save(vm.professor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:professorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
