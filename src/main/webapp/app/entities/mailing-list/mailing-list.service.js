(function() {
    'use strict';
    angular
        .module('internShippingApp')
        .factory('MailingList', MailingList);

    MailingList.$inject = ['$resource', 'DateUtils'];

    function MailingList ($resource, DateUtils) {
        var resourceUrl =  'api/mailing-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateCreated = DateUtils.convertLocalDateFromServer(data.dateCreated);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateCreated = DateUtils.convertLocalDateToServer(data.dateCreated);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateCreated = DateUtils.convertLocalDateToServer(data.dateCreated);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
