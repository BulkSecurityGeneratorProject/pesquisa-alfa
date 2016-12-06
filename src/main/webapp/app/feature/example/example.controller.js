(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ExampleController', ExampleController);

    ExampleController.$inject = ['Principal', 'Auth'];

    function ExampleController (Principal, Auth) {
        var vm = this;

        // controller logic goes here

       
    }
})();
