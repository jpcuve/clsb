/**
 * Created by jpc on 22-10-16.
 */
angular.module("clsb", ["ngResource", "ngRoute"])
    .constant("constant", {
    })
    .config(["$resourceProvider", function($resourceProvider){
        $resourceProvider.defaults.stripTrailingSlashes = false;
    }])
    .config(["$routeProvider", function($routeProvider){
        $routeProvider
            .when("/missions/:id", { templateUrl: "view-mission-item.html"})
            .when("/crew-members/:id", { templateUrl: "view-crew-member-item.html"})
            .when("/missions", { templateUrl: "view-mission-list.html"})
            .when("/crew-members", { templateUrl: "view-crew-member-list.html"})
            .when("/", { templateUrl: "view-day.html"})
            .otherwise({ redirectTo: "/"})
    }])
    .factory("endPoint", ["$log", "$location", function($log, $location){
        return function(ep){
            "use strict";
            var base = $location.port() >= 63342 ? ["http://", $location.host(), ":8080"] : [$location.protocol(), "://", $location.host(), ":", $location.port()];
            base.push("/clsb/api", ep);
            return base.join("");
        }
    }])
    .factory("res", ["$resource", "endPoint", function($resource, endPoint){
        return {
            commandResource: $resource(endPoint("/command/:cmd")),
            currenciesResource: $resource(endPoint("/missions/:id"))
        }
    }])
    .controller("clsbController", ["$log", "$scope", "$routeParams", "$location", "res", function($log, $scope, $routeParams, $location, res){
        "use strict";
        $scope.title = "CLSB Sim";

        $scope.command = function(cmd){
            res.commandResource.get({cmd: cmd});
        }
    }])
    .directive("daySvg", function(){
        return {
            restrict: "E",
            templateUrl: "day.svg"

        };
    })
;

