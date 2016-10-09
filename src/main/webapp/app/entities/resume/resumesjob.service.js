(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .factory('ResumesJob', ResumesJob);

    ResumesJob.$inject = ['$resource'];

    function ResumesJob($resource) {
        var resourceUrl =  'api/job/:id/resumes';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
