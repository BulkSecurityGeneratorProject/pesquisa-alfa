(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('Banca', Banca);

    Banca.$inject = ['$resource', 'DateUtils'];

    function Banca ($resource, DateUtils) {
        var resourceUrl =  'api/banca-avaliadora/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.horario = DateUtils.convertDateTimeFromServer(data.horario);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
