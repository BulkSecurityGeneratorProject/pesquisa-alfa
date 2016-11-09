(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('MestrandoDialogController', MestrandoDialogController);

    MestrandoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Mestrando', 'Aluno'];

    function MestrandoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Mestrando, Aluno) {
        var vm = this;

        vm.mestrando = entity;
        vm.clear = clear;
        vm.save = save;
        vm.alunos = Aluno.query({filter: 'mestrando-is-null'});
        $q.all([vm.mestrando.$promise, vm.alunos.$promise]).then(function() {
            if (!vm.mestrando.aluno || !vm.mestrando.aluno.id) {
                return $q.reject();
            }
            return Aluno.get({id : vm.mestrando.aluno.id}).$promise;
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
            if (vm.mestrando.id !== null) {
                Mestrando.update(vm.mestrando, onSaveSuccess, onSaveError);
            } else {
                Mestrando.save(vm.mestrando, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:mestrandoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
