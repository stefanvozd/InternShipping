(function() {
    'use strict';
    angular
        .module('internShippingApp')
        .factory('Resume', Resume);

    Resume.$inject = ['$resource', 'DateUtils'];

    function Resume ($resource, DateUtils) {
        var resourceUrl =  'api/resumes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.birthDate = DateUtils.convertLocalDateFromServer(data.birthDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.birthDate = DateUtils.convertLocalDateToServer(data.birthDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.birthDate = DateUtils.convertLocalDateToServer(data.birthDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
