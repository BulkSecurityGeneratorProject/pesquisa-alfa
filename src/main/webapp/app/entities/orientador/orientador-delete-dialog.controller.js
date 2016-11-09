(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('OrientadorDeleteController',OrientadorDeleteController);

    OrientadorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Orientador'];

    function OrientadorDeleteController($uibModalInstance, entity, Orientador) {
        var vm = this;

        vm.orientador = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Orientador.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
