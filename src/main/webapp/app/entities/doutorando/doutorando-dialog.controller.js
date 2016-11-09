(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('DoutorandoDialogController', DoutorandoDialogController);

    DoutorandoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Doutorando', 'Aluno'];

    function DoutorandoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Doutorando, Aluno) {
        var vm = this;

        vm.doutorando = entity;
        vm.clear = clear;
        vm.save = save;
        vm.alunos = Aluno.query({filter: 'doutorando-is-null'});
        $q.all([vm.doutorando.$promise, vm.alunos.$promise]).then(function() {
            if (!vm.doutorando.aluno || !vm.doutorando.aluno.id) {
                return $q.reject();
            }
            return Aluno.get({id : vm.doutorando.aluno.id}).$promise;
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
            if (vm.doutorando.id !== null) {
                Doutorando.update(vm.doutorando, onSaveSuccess, onSaveError);
            } else {
                Doutorando.save(vm.doutorando, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:doutorandoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
