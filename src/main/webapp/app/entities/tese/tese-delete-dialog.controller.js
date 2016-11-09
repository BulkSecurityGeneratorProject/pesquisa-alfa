(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('TeseDeleteController',TeseDeleteController);

    TeseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tese'];

    function TeseDeleteController($uibModalInstance, entity, Tese) {
        var vm = this;

        vm.tese = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tese.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
