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
      when('/applications/:appName/environments/:envName/instances/:instanceId', {
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
  factory('Instances', ['$resource', function($resource) {
    return $resource('/api/applications/:appName/environments/:envName/instances/:instanceId');
  }]).
  controller('ApplicationsController', ['Applications', 'Environments', '$scope', '$routeParams', '$timeout', function(Applications, Environments, $scope, $routeParams, $timeout) {
    $scope.$routeParams = $routeParams;

    $scope.load = function() {
      console.log('LOAD');
      Applications.query({}, function(applications) {
        $scope.applications = applications;
        $scope.applications._loaded = true;
        angular.forEach($scope.applications, function(app) {
          app.environments._loaded = true;
        });
      });
    };

    $scope.load();

    $timeout($scope.load, 15000);
  }]).
  controller('HomeController', [function() {
    
  }]).
  controller('EnvironmentController', ['Environments', 'Instances', '$http', '$scope', '$location', '$routeParams', '$log', function(Environments, Instances, $http, $scope, $location, $routeParams, $log) {
    var params = {appName: $routeParams.appName, envName: $routeParams.envName};

    $scope.$routeParams = $routeParams;

    $scope.env = Environments.get(params, function() {
      $scope.env._loaded = true;
    });

    $scope.instances = Instances.query(params, function() {
      $scope.instances._loaded = true;
      if (!$routeParams.instanceId && $scope.instances.length > 0) {
        $location.path($location.path() + '/instances/' + $scope.instances[0].id);
      }
    });

    $scope.$watch('$routeParams.instanceId', function() {
      $scope.instance = Instances.get(angular.extend({}, params, {instanceId: $routeParams.instanceId}), function() {
        $scope.instance._loaded = true;
        $scope.loadLog();
      });
    });

    $scope.loadLog = function() {
      $http.get('/api/hosts/' + $scope.instance.privateIpAddress + '/logs/main').
        success(function(log) {
          $scope.log = log;
          setTimeout($scope.tailLog, 10);
        }).
        error(function(err) {
          $log.error('Error loading logs for host ', $scope.instance.privateIpAddress, err);
        });
    };

    $scope.tailLog = function() {
      var log = document.getElementById('log');
      log.scrollTop = log.scrollHeight;
    };
  }]);

