(function() {
    'use strict';

    angular
        .module('internShippingApp')
        .factory('MyResume', MyResume);

    MyResume.$inject = ['$resource'];

    function MyResume($resource) {
        var resourceUrl =  'api/myresume/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
