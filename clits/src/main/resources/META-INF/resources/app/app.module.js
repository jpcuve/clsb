"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require("@angular/core");
var router_1 = require("@angular/router");
var platform_browser_1 = require("@angular/platform-browser");
var app_component_1 = require("./app.component");
var view_day_component_1 = require("./view-day.component");
var view_account_component_1 = require("./view-account.component");
var some_service_1 = require("./some.service");
var http_1 = require("@angular/http");
var clsb_service_1 = require("./clsb.service");
var position_component_1 = require("./position.component");
var routes = [
    { path: 'day', component: view_day_component_1.ViewDayComponent },
    { path: 'account/:name', component: view_account_component_1.ViewAccountComponent },
    { path: '**', component: view_day_component_1.ViewDayComponent }
];
var AppModule = (function () {
    function AppModule() {
    }
    return AppModule;
}());
AppModule = __decorate([
    core_1.NgModule({
        imports: [platform_browser_1.BrowserModule, router_1.RouterModule.forRoot(routes), http_1.HttpModule, http_1.JsonpModule],
        declarations: [app_component_1.AppComponent, view_day_component_1.ViewDayComponent, view_account_component_1.ViewAccountComponent, position_component_1.PositionComponent],
        providers: [some_service_1.SomeService, clsb_service_1.ClsbService],
        bootstrap: [app_component_1.AppComponent]
    })
], AppModule);
exports.AppModule = AppModule;
//# sourceMappingURL=app.module.js.map