(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC06DialogController', UC06DialogController);

    UC06DialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UC06', 'Aluno', 'Orientador'];

    function UC06DialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, UC06, Aluno, Orientador) {
        var vm = this;

        vm.uc06 = entity;
        vm.clear = clear;
        vm.save = save;
        vm.alunos = Aluno.query({filter: 'uc06-is-null'});
        $q.all([vm.uc06.$promise, vm.alunos.$promise]).then(function() {
            if (!vm.uc06.aluno || !vm.uc06.aluno.id) {
                return $q.reject();
            }
            return Aluno.get({id : vm.uc06.aluno.id}).$promise;
        }).then(function(aluno) {
            vm.alunos.push(aluno);
        });
        vm.orientadors = Orientador.query({filter: 'uc06-is-null'});
        $q.all([vm.uc06.$promise, vm.orientadors.$promise]).then(function() {
            if (!vm.uc06.orientador || !vm.uc06.orientador.id) {
                return $q.reject();
            }
            return Orientador.get({id : vm.uc06.orientador.id}).$promise;
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
            if (vm.uc06.id !== null) {
                uc06.update(vm.uc06, onSaveSuccess, onSaveError);
            } else {
                UC06.save(vm.uc06, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:uc06Update', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
