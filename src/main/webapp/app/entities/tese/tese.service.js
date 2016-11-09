(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('Tese', Tese);

    Tese.$inject = ['$resource'];

    function Tese ($resource) {
        var resourceUrl =  'api/tese/:id';

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
