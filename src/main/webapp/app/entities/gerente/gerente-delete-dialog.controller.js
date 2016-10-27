(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('GerenteDeleteController',GerenteDeleteController);

    GerenteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Gerente'];

    function GerenteDeleteController($uibModalInstance, entity, Gerente) {
        var vm = this;

        vm.gerente = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Gerente.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
