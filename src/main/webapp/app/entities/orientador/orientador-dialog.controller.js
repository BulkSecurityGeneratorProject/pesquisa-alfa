(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('OrientadorDialogController', OrientadorDialogController);

    OrientadorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Orientador', 'Professor'];

    function OrientadorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Orientador, Professor) {
        var vm = this;

        vm.orientador = entity;
        vm.clear = clear;
        vm.save = save;
        vm.professors = Professor.query({filter: 'orientador-is-null'});
        $q.all([vm.orientador.$promise, vm.professors.$promise]).then(function() {
            if (!vm.orientador.professor || !vm.orientador.professor.id) {
                return $q.reject();
            }
            return Professor.get({id : vm.orientador.professor.id}).$promise;
        }).then(function(professor) {
            vm.professors.push(professor);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orientador.id !== null) {
                Orientador.update(vm.orientador, onSaveSuccess, onSaveError);
            } else {
                Orientador.save(vm.orientador, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:orientadorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
