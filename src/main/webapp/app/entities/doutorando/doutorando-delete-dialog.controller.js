(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('DoutorandoDeleteController',DoutorandoDeleteController);

    DoutorandoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Doutorando'];

    function DoutorandoDeleteController($uibModalInstance, entity, Doutorando) {
        var vm = this;

        vm.doutorando = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Doutorando.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
