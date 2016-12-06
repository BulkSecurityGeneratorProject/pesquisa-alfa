(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('Orientador', Orientador);

    Orientador.$inject = ['$resource', 'DateUtils'];

    function Orientador ($resource, DateUtils) {
        var resourceUrl =  'api/orientador/:id';

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
