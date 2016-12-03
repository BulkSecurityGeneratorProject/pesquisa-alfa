(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('UC5DeleteController',UC5DeleteController);

    UC5DeleteController.$inject = ['$uibModalInstance', 'entity', 'UC5'];

    function UC5DeleteController($uibModalInstance, entity, UC5) {
        var vm = this;

        vm.uc5 = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UC5.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
