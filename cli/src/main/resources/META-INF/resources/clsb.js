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
            .when("/day", { templateUrl: "view-day.html"})
            .when("/account", { templateUrl: "view-account.html"})
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
            currencyResource: $resource(endPoint("/currencies/:id"))
        }
    }])
    .controller("clsbController", ["$log", "$scope", "$routeParams", "$location", "res", function($log, $scope, $routeParams, $location, res){
        "use strict";
        $scope.title = "CLSB Sim";

        $scope.currencies = res.currencyResource.query(function(cs){
            $log.log("currencies retrieved: ", cs);
        });

        $scope.command = function(cmd){
            res.commandResource.get({cmd: cmd});
        };

        var ws = new WebSocket("ws://localhost:8080/clsb/scheduler");

        ws.onopen = function(event){
            ws.send("coucou");
        };

        ws.onmessage = function(event){
            $log.log("Event data: " + event.data);
            var data = JSON.parse(event.data);
            if (data){
                $scope.time = data.localTime;
            }
        };

    }])
    .directive("dayOverview", ["$log", function($log){
        return {
            restrict: "E",
            templateUrl: "day.svg",
            scope: {
                currencies: "="
            },
            link: function(scope){
                scope.time = function(t){
                    var colon = t.indexOf(":");
                    if (colon >= 0){
                        return parseInt(t.substring(0, colon)) * 60 + parseInt(t.substring(colon + 1));
                    }
                    return 0;
                }
            }
        };
    }])
;

