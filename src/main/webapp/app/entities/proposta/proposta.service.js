(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('Proposta', Proposta);

    Proposta.$inject = ['$resource'];

    function Proposta ($resource) {
        var resourceUrl =  'api/propostas/:id';

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
