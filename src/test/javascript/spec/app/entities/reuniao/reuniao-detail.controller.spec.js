'use strict';

describe('Controller Tests', function() {

    describe('Reuniao Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockReuniao, MockAluno, MockOrientador;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockReuniao = jasmine.createSpy('MockReuniao');
            MockAluno = jasmine.createSpy('MockAluno');
            MockOrientador = jasmine.createSpy('MockOrientador');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Reuniao': MockReuniao,
                'Aluno': MockAluno,
                'Orientador': MockOrientador
            };
            createController = function() {
                $injector.get('$controller')("ReuniaoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pesquisaalfaApp:reuniaoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
