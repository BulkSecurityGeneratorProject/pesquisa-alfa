(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('Proposta', Proposta);

    Proposta.$inject = ['$resource', 'DateUtils'];

    function Proposta ($resource, DateUtils) {
        var resourceUrl =  'api/propostas/:id';

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
