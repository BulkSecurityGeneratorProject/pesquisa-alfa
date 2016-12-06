(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('Artigo', Artigo);

    Artigo.$inject = ['$resource'];

    function Artigo ($resource) {
        var resourceUrl =  'api/artigos/:id';

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
