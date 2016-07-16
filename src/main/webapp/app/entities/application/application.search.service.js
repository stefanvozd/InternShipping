(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .factory('ApplicationSearch', ApplicationSearch);

    ApplicationSearch.$inject = ['$resource'];

    function ApplicationSearch($resource) {
        var resourceUrl =  'api/_search/applications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
