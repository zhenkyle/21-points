'use strict';

angular.module('21pointsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('home', {
                parent: 'site',
                url: '/',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/main.html',
                        controller: 'MainController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        return $translate.refresh();
                    }]
                }
            })
            .state('point.add', {
                parent: 'point',
                url: 'add/point',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/point/point-dialog.html',
                        controller: 'PointDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    date: null,
                                    exercise: null,
                                    meals: null,
                                    alcohol: null,
                                    notes: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('home', null, { reload: true });
                    }, function() {
                        $state.go('home');
                    })
                }]
            })
            .state('weight.add', {
                url: 'add/weight',
                parent: 'home',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function ($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/weight/weight-dialog.html',
                        controller: 'WeightDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {timestamp: null, weight: null, id: null};
                            }
                        }
                    }).result.then(function (result) {
                        $state.go('home', null, {reload: true});
                    }, function () {
                        $state.go('home');
                    })
                }]
            })
            .state('bloodPressure.add', {
                parent: 'home',
                url: 'add/bp',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/bloodPressure/bloodPressure-dialog.html',
                        controller: 'BloodPressureDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {timestamp: null, systolic: null, diastolic: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('home', null, { reload: true });
                    }, function() {
                        $state.go('home');
                    })
                }]
            })
            .state('preference.add', {
                parent: 'home',
                url: '/add/preference',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/preference/preference-dialog.html',
                        controller: 'PreferenceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function (Preference) {
                                return Preference.user().$promise;
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('home', null, { reload: true });
                    }, function() {
                        $state.go('home');
                    })
                }]
            });
    });
