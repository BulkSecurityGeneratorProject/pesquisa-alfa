(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('TeseDialogController', TeseDialogController);

    TeseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Tese', 'Proposta'];

    function TeseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Tese, Proposta) {
        var vm = this;

        vm.tese = entity;
        vm.clear = clear;
        vm.save = save;
        vm.propostas = Proposta.query({filter: 'tese-is-null'});
        $q.all([vm.tese.$promise, vm.propostas.$promise]).then(function() {
            if (!vm.tese.proposta || !vm.tese.proposta.id) {
                return $q.reject();
            }
            return Proposta.get({id : vm.tese.proposta.id}).$promise;
        }).then(function(proposta) {
            vm.propostas.push(proposta);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tese.id !== null) {
                Tese.update(vm.tese, onSaveSuccess, onSaveError);
            } else {
                Tese.save(vm.tese, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pesquisaalfaApp:teseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
