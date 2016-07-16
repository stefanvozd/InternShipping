(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .factory('ResumeSearch', ResumeSearch);

    ResumeSearch.$inject = ['$resource'];

    function ResumeSearch($resource) {
        var resourceUrl =  'api/_search/resumes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
