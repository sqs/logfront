angular.module('logfront', ['ngResource']).
  config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $locationProvider.html5Mode(true);
    $routeProvider.
      when('/', {
        controller: 'HomeController',
        templateUrl: '/assets/templates/home.html'
      }).
      when('/applications/:appName/environments/:envName', {
        controller: 'EnvironmentController',
        templateUrl: '/assets/templates/environment.html'
      }).
      otherwise({redirectTo: '/'});
  }]).
  factory('Applications', ['$resource', function($resource) {
    return $resource('/api/applications/:appName');
  }]).
  factory('Environments', ['$resource', function($resource) {
    return $resource('/api/applications/:appName/environments/:envName');
  }]).
  controller('ApplicationsController', ['Applications', 'Environments', '$scope', '$routeParams', function(Applications, Environments, $scope, $routeParams) {
    $scope.$routeParams = $routeParams;

    $scope.applications = Applications.query({}, function() {
      $scope.applications._loaded = true;
      angular.forEach($scope.applications, function(app) {
        app.environments = Environments.query({appName: app.applicationName}, function() {
          app.environments._loaded = true;
        });
      });
    });
  }]).
  controller('HomeController', [function() {
    
  }]).
  controller('EnvironmentController', ['Environments', '$scope', '$routeParams', function(Environments, $scope, $routeParams) {
    $scope.env = Environments.get({appName: $routeParams.appName, envName: $routeParams.envName}, function() {
      $scope._loaded = true;
    });
  }]);

