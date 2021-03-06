/**
 * Created by jpc on 22-10-16.
 */
angular.module("clsb", ["ngResource", "ngRoute"])
    .constant("constant", {
        legs : function(position){
            var a = [], ccy, o;
            for (ccy in position) {
                if (position.hasOwnProperty(ccy)){
                    o = {};
                    o[ccy] = position[ccy];
                    a.push(o);
                }
            }
            return a;
        }
    })
    .config(["$resourceProvider", function($resourceProvider){
        $resourceProvider.defaults.stripTrailingSlashes = false;
    }])
    .config(["$routeProvider", function($routeProvider){
        $routeProvider
            .when("/day", { templateUrl: "view-day.html"})
            .when("/account/:name", { templateUrl: "view-account.html"})
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
            currencyResource: $resource(endPoint("/currencies/:id")),
            accountResource: $resource(endPoint("/accounts/:name")),
            movementResource: $resource(endPoint("/movements/:name")),
            bankResource: $resource(endPoint("/bank"))
        }
    }])
    .controller("accountController", ["$log", "$scope", "$route", "res", function($log, $scope, $route, res){
        "use strict";
        function update(){
            var accountName = $route.current.params.name;
            $scope.account = res.accountResource.get({name: accountName});
            $scope.movements = res.movementResource.query({name: accountName}, function(movements){
                movements.forEach(function(movement){
                    movement.target = movement.dest.name === accountName;
                })

            });
        }

        update();

        $scope.$on("now", function(event){
            $log.log("now event received");
            update();
        });

    }])
    .controller("dayController", ["$log", "$scope", "$resource", "endPoint", function($log, $scope, $resource, endPoint){
        "use strict";
        function update(){
            $scope.positions = $resource(endPoint("/positions")).get();
        }

        update();

        $scope.$on("now", function(event){
            $log.log("now event received");
            update();
        });
    }])
    .controller("clsbController", ["$log", "$scope", "$routeParams", "$location", "res", "constant", function($log, $scope, $routeParams, $location, res, constant) {
        "use strict";
        $scope.title = "CLSB Sim";

        $scope.currencies = res.currencyResource.query();
        $scope.bank = res.bankResource.get();

        $scope.constant = constant;

        $scope.command = function(cmd){
            res.commandResource.get({cmd: cmd}, function(now){
                $scope.now = now;
            });
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
                $scope.$broadcast("now");
            }
        };

    }])
    .directive("position", ["$log", function($log){
        return {
            restrict: "E",
            templateUrl: "position.html",
            scope: {
                position: "="
            },
            link: function(scope){

            }
        };
    }])
    .directive("dayOverview", ["$log", function($log){
        return {
            restrict: "E",
            templateUrl: "day.svg",
            scope: {
                now: "=",
                bank: "=",
                currencies: "="
            },
            link: function(scope){
                scope.time = function(t){
                    if (t){
                        var colon = t.indexOf(":");
                        if (colon >= 0){
                            return parseInt(t.substring(0, colon)) * 60 + parseInt(t.substring(colon + 1));
                        }
                    }
                    return 0;
                };

                scope.hours = function(){
                    return new Array(24);
                };
            }
        };
    }])
;

