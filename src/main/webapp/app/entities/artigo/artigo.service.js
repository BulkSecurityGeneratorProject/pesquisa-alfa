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
                        data.dataApresentacao = DateUtils.convertLocalDateFromServer(data.dataApresentacao);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dataApresentacao = DateUtils.convertLocalDateToServer(copy.dataApresentacao);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dataApresentacao = DateUtils.convertLocalDateToServer(copy.dataApresentacao);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
