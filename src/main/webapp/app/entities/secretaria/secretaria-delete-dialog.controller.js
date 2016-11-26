(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('SecretariaDeleteController',SecretariaDeleteController);

    SecretariaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Secretaria'];

    function SecretariaDeleteController($uibModalInstance, entity, Secretaria) {
        var vm = this;

        vm.secretaria = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Secretaria.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
