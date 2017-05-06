import {Component} from '@angular/core';

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
    <nav>
        <a routerLink="/day">Day</a>
        <a routerLink="/account/dadada">Account</a>
    </nav>
    <router-outlet></router-outlet>
    `
})
export class AppComponent {
    public name: string = 'Jean-Pierre';
    public friends: string[] = ['Nicolas', 'Patrick', 'Philippe'];
    public clickMessage: string = '';

    public pushed(): void {
        console.debug("clicked");
        this.clickMessage = "Hello!";
    }
}
