(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('Orientador', Orientador);

    Orientador.$inject = ['$resource'];

    function Orientador ($resource) {
        var resourceUrl =  'api/orientadors/:id';

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
