(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC11DeleteController',UC11DeleteController);

    UC11DeleteController.$inject = ['$uibModalInstance', 'entity', 'UC11'];

    function UC11DeleteController($uibModalInstance, entity, UC11) {
        var vm = this;

        vm.uc5 = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UC11.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
