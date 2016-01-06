'use strict';

describe('Controller Tests', function() {

    describe('Preferences Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreferences;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreferences = jasmine.createSpy('MockPreferences');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Preferences': MockPreferences
            };
            createController = function() {
                $injector.get('$controller')("PreferencesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = '21pointsApp:preferencesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
