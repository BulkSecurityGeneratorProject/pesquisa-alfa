(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ApresentacoesArtigoDeleteController',ApresentacoesArtigoDeleteController);

    ApresentacoesArtigoDeleteController.$inject = ['$uibModalInstance', 'entity', 'ApresentacoesArtigo'];

    function ApresentacoesArtigoDeleteController($uibModalInstance, entity, ApresentacoesArtigo) {
        var vm = this;

        vm.apresentacoesArtigo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ApresentacoesArtigo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
