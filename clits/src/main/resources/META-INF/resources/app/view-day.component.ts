import {Component, OnInit} from '@angular/core';
import {ClsbService} from "./clsb.service";

@Component({
    template: `
<h2>View day</h2>
<pre>{{data}}</pre>
<ul>
    <li *ngFor="let a of accounts">{{a}}</li>
</ul>
`
})
export class ViewDayComponent implements OnInit {
    errorMessage: string;
    data: Map<string, any>;
    accounts: string[];

    constructor(private clsbService: ClsbService){
    }

    ngOnInit(): void {
        this.getPositions();
    }

    getPositions(): void {
        this.clsbService.getPositions().subscribe(
            o => {
                this.data = o;
                this.accounts = Object.keys(this.data);
            },
            error => this.errorMessage = error
        );
    }

}
