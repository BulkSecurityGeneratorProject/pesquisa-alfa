(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('Mestrando', Mestrando);

    Mestrando.$inject = ['$resource'];

    function Mestrando ($resource) {
        var resourceUrl =  'api/mestrandos/:id';

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
