(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC03DialogController', UC03DialogController);

    UC03DialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UC03', 'Aluno', 'Orientador'];

    function UC03DialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, UC03, Aluno, Orientador) {
        var vm = this;

        vm.uc03 = entity;
        vm.clear = clear;
        vm.save = save;
        vm.orientador = Orientador.query({filter: 'uc03-is-null'});
        $q.all([vm.uc03.$promise, vm.orientadors.$promise]).then(function() {
            if (!vm.uc03.orientador || !vm.uc03.orientador.id) {
                return $q.reject();
            }
            return Orientador.get({id : vm.uc03.orientador.id}).$promise;
        }).then(function(orientador) {
            vm.orientador.push(orientador);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.uc03.id !== null) {
                UC03.update(vm.uc03, onSaveSuccess, onSaveError);
            } else {
                UC03.save(vm.uc03, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:uc03Update', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
