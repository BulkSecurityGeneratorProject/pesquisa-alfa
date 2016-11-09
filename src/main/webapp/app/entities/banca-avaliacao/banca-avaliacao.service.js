(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('BancaAvaliacao', BancaAvaliacao);

    BancaAvaliacao.$inject = ['$resource', 'DateUtils'];

    function BancaAvaliacao ($resource, DateUtils) {
        var resourceUrl =  'api/banca-avaliacaos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dataHoraApresentacao = DateUtils.convertDateTimeFromServer(data.dataHoraApresentacao);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
