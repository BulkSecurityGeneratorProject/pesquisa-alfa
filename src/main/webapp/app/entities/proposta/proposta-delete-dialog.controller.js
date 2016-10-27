(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('PropostaDeleteController',PropostaDeleteController);

    PropostaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Proposta'];

    function PropostaDeleteController($uibModalInstance, entity, Proposta) {
        var vm = this;

        vm.proposta = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Proposta.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
