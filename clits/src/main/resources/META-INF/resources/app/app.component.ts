import {Component} from '@angular/core';
import {ClsbService} from "./clsb.service";

@Component({
    selector: 'my-app',
    template: `
    <button (click)="command('reset()')">Reset</button>
    <button (click)="command('step()')">Step</button>
    <button (click)="command('all()')">All</button>
    <overview [current]="now"></overview>
    <nav>
        <a routerLink="day" routerLinkActive="active">Day</a>
        <a routerLink="account/dadada" routerLinkActive="active">Account</a>
    </nav>
    <router-outlet></router-outlet>
    `
})
export class AppComponent {
    now: string = '00:00';

    constructor(
        private service: ClsbService
    ){
    }

    command(cmd: string): void {
        this.service.sendCommand(cmd).subscribe(t => this.now = t);
    }

}
