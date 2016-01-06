'use strict';

angular.module('21pointsApp')
    .controller('PreferencesDetailController', function ($scope, $rootScope, $stateParams, entity, Preferences) {
        $scope.preferences = entity;
        $scope.load = function (id) {
            Preferences.get({id: id}, function(result) {
                $scope.preferences = result;
            });
        };
        var unsubscribe = $rootScope.$on('21pointsApp:preferencesUpdate', function(event, result) {
            $scope.preferences = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
