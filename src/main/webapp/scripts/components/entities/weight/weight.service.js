'use strict';

angular.module('21pointsApp')
    .factory('Weight', function ($resource, DateUtils) {
        return $resource('api/weights/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.timestamp = DateUtils.convertLocaleDateFromServer(data.timestamp);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.timestamp = DateUtils.convertLocaleDateToServer(data.timestamp);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.timestamp = DateUtils.convertLocaleDateToServer(data.timestamp);
                    return angular.toJson(data);
                }
            }
        });
    });
