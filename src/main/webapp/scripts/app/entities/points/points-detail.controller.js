'use strict';

angular.module('21pointsApp')
    .controller('PointsDetailController', function ($scope, $rootScope, $stateParams, entity, Points, User) {
        $scope.points = entity;
        $scope.load = function (id) {
            Points.get({id: id}, function(result) {
                $scope.points = result;
            });
        };
        var unsubscribe = $rootScope.$on('21pointsApp:pointsUpdate', function(event, result) {
            $scope.points = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
