'use strict';

angular.module('21pointsApp').controller('PointDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Point', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, Point, User) {

        // defaults for new entries
        if (!entity.id) {
            entity.date = new Date();
            entity.exercise = 1;
            entity.meals = 1;
            entity.alcohol = 1;
        }

        $scope.point = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Point.get({id : id}, function(result) {
                $scope.point = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('21pointsApp:pointUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.point.id != null) {
                Point.update($scope.point, onSaveSuccess, onSaveError);
            } else {
                Point.save($scope.point, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDate = {};

        $scope.datePickerForDate.status = {
            opened: false
        };

        $scope.datePickerForDateOpen = function($event) {
            $scope.datePickerForDate.status.opened = true;
        };
}]);
