'use strict';

describe('Controller Tests', function() {

    describe('ApresentacoesArtigo Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockApresentacoesArtigo, MockArtigo;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockApresentacoesArtigo = jasmine.createSpy('MockApresentacoesArtigo');
            MockArtigo = jasmine.createSpy('MockArtigo');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ApresentacoesArtigo': MockApresentacoesArtigo,
                'Artigo': MockArtigo
            };
            createController = function() {
                $injector.get('$controller')("ApresentacoesArtigoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pesquisaalfaApp:apresentacoesArtigoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
