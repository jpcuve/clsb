import {Component, OnInit} from '@angular/core';
import {ClsbService} from "./clsb.service";

@Component({
    template: `
    <h2>View day</h2>
    <pre>{{res}}</pre>
    `
})
export class ViewDayComponent implements OnInit {
    errorMessage: string;
    res: any;

    constructor(private clsbService: ClsbService){
    }

    ngOnInit(): void {
        this.getPositions();
    }

    getPositions(): void {
        this.clsbService.getPositions().subscribe(
            data => this.res = JSON.stringify(data.json()),
            error => this.errorMessage = error
        );
    }

}
