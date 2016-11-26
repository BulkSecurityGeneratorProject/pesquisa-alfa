(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('Secretaria', Secretaria);

    Secretaria.$inject = ['$resource'];

    function Secretaria ($resource) {
        var resourceUrl =  'api/secretarias/:id';

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
