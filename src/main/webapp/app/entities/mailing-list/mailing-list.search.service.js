(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .factory('MailingListSearch', MailingListSearch);

    MailingListSearch.$inject = ['$resource'];

    function MailingListSearch($resource) {
        var resourceUrl =  'api/_search/mailing-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
