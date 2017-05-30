import { NgModule }      from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent }  from './app.component';
import {ViewDayComponent} from "./view-day.component";
import {ViewAccountComponent} from "./view-account.component";
import {SomeService} from "./some.service";
import {HttpModule, JsonpModule} from "@angular/http";
import {ClsbService} from "./clsb.service";
import {PositionComponent} from "./position.component";
import {OverviewComponent} from "./overview.component";

const routes: Routes = [
    { path: 'day', component: ViewDayComponent },
    { path: 'account/:name', component: ViewAccountComponent },
    { path: '**', component: ViewDayComponent }
];

@NgModule({
    imports: [BrowserModule, RouterModule.forRoot(routes), HttpModule, JsonpModule],
    declarations: [AppComponent, ViewDayComponent, ViewAccountComponent, PositionComponent, OverviewComponent],
    providers: [SomeService, ClsbService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
