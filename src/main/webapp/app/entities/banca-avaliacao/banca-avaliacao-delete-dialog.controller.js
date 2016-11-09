(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('BancaAvaliacaoDeleteController',BancaAvaliacaoDeleteController);

    BancaAvaliacaoDeleteController.$inject = ['$uibModalInstance', 'entity', 'BancaAvaliacao'];

    function BancaAvaliacaoDeleteController($uibModalInstance, entity, BancaAvaliacao) {
        var vm = this;

        vm.bancaAvaliacao = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BancaAvaliacao.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
