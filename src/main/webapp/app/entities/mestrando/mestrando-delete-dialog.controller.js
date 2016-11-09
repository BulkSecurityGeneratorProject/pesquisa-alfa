(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('MestrandoDeleteController',MestrandoDeleteController);

    MestrandoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Mestrando'];

    function MestrandoDeleteController($uibModalInstance, entity, Mestrando) {
        var vm = this;

        vm.mestrando = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Mestrando.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
