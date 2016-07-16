'use strict';

describe('Controller Tests', function() {

    describe('Resume Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockResume, MockUser, MockApplication;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockResume = jasmine.createSpy('MockResume');
            MockUser = jasmine.createSpy('MockUser');
            MockApplication = jasmine.createSpy('MockApplication');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Resume': MockResume,
                'User': MockUser,
                'Application': MockApplication
            };
            createController = function() {
                $injector.get('$controller')("ResumeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'internShippingApp:resumeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
