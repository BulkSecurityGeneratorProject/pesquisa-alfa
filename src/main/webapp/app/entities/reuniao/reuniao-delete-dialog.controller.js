(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ReuniaoDeleteController',ReuniaoDeleteController);

    ReuniaoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Reuniao'];

    function ReuniaoDeleteController($uibModalInstance, entity, Reuniao) {
        var vm = this;

        vm.reuniao = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Reuniao.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
