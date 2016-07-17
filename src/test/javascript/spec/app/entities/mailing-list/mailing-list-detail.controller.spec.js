'use strict';

describe('Controller Tests', function() {

    describe('MailingList Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMailingList;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMailingList = jasmine.createSpy('MockMailingList');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MailingList': MockMailingList
            };
            createController = function() {
                $injector.get('$controller')("MailingListDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'internShippingApp:mailingListUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
