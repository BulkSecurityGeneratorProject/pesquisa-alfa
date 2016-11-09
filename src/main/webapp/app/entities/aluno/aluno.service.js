(function() {
    'use strict';
    angular
        .module('pesquisaalfaApp')
        .factory('Aluno', Aluno);

    Aluno.$inject = ['$resource', 'DateUtils'];

    function Aluno ($resource, DateUtils) {
        var resourceUrl =  'api/alunos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dataMatricula = DateUtils.convertLocalDateFromServer(data.dataMatricula);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dataMatricula = DateUtils.convertLocalDateToServer(copy.dataMatricula);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dataMatricula = DateUtils.convertLocalDateToServer(copy.dataMatricula);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
