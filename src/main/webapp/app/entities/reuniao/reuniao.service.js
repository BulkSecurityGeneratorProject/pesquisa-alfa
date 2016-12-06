(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('Reuniao', Reuniao);

    Reuniao.$inject = ['$resource', 'DateUtils'];

    function Reuniao ($resource, DateUtils) {
        var resourceUrl =  'api/reuniaos/:id';

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
