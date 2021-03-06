'use strict';

describe('Controller Tests', function() {

    describe('Proposta Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProposta, MockAluno, MockOrientador;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProposta = jasmine.createSpy('MockProposta');
            MockAluno = jasmine.createSpy('MockAluno');
            MockOrientador = jasmine.createSpy('MockOrientador');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Proposta': MockProposta,
                'Aluno': MockAluno,
                'Orientador': MockOrientador
            };
            createController = function() {
                $injector.get('$controller')("PropostaDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pesquisaalfaApp:propostaUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
