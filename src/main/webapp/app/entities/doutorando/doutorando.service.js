(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('Doutorando', Doutorando);

    Doutorando.$inject = ['$resource'];

    function Doutorando ($resource) {
        var resourceUrl =  'api/doutorandos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
