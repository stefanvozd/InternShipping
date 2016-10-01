(function() {
    'use strict';
    angular
        .module('internShippingApp')
        .factory('OurJobs', Job);

    Job.$inject = ['$resource', 'DateUtils'];

    function Job ($resource, DateUtils) {
        var resourceUrl =  'api/jobs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                        data.activeUntil = DateUtils.convertLocalDateFromServer(data.activeUntil);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.activeUntil = DateUtils.convertLocalDateToServer(data.activeUntil);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.activeUntil = DateUtils.convertLocalDateToServer(data.activeUntil);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
