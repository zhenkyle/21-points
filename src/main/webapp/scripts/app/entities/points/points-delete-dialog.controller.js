'use strict';

angular.module('21pointsApp')
	.controller('PointsDeleteController', function($scope, $uibModalInstance, entity, Points) {

        $scope.points = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Points.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
