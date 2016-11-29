(function() {
    'use strict';

    angular
        .module('pesquisaalfaApp')
        .controller('ExampleController', ExampleController);

    ExampleController.$inject = ['Principal', 'Auth', 'JhiLanguageService', '$translate'];

    function ExampleController (Principal, Auth, JhiLanguageService, $translate) {
        var vm = this;

        // controller logic goes here

       
    }
})();
