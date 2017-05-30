import {Component} from '@angular/core';
import {SomeService} from "./some.service";

@Component({
    selector: 'my-app',
    template: `
    <h1>Hello {{name}}</h1>
    <p *ngIf="friends.length > 2">There are many friends</p>
    <ul>
        <li *ngFor="let friend of friends">{{friend}}</li>
    </ul>
    <button (click)="pushed()">Click!</button>
    <span>{{clickMessage}}</span>
    <overview></overview>
    <nav>
        <a routerLink="day" routerLinkActive="active">Day</a>
        <a routerLink="account/dadada" routerLinkActive="active">Account</a>
    </nav>
    <router-outlet></router-outlet>
    `
})
export class AppComponent {
    public name: string = 'Jules';
    public friends: string[] = ['Nicolas', 'Patrick', 'Philippe'];
    public clickMessage: string = '';

    constructor(
        private service: SomeService
    ){
    }

    public pushed(): void {
        console.debug("clicked");
        this.clickMessage = "Hello!";
        this.service.print();
    }
}
