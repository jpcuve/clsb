import { NgModule }      from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent }  from './app.component';
import {ViewDayComponent} from "./view-day.component";
import {ViewAccountComponent} from "./view-account.component";

const routes: Routes = [
    { path: 'day', component: ViewDayComponent },
    { path: 'account/:name', component: ViewAccountComponent },
    { path: '**', component: ViewDayComponent }
];

@NgModule({
    imports: [BrowserModule, RouterModule.forRoot(routes)],
    declarations: [AppComponent, ViewDayComponent, ViewAccountComponent],
    bootstrap: [AppComponent]
})
export class AppModule {
}
