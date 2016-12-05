(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('Artigo', Artigo);

    Artigo.$inject = ['$resource', 'DateUtils'];

    function Artigo ($resource, DateUtils) {
        var resourceUrl =  'api/artigos/:id';

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
