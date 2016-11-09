(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ArtigoDeleteController',ArtigoDeleteController);

    ArtigoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Artigo'];

    function ArtigoDeleteController($uibModalInstance, entity, Artigo) {
        var vm = this;

        vm.artigo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Artigo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
