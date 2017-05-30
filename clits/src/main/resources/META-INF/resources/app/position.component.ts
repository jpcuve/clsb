/**
 * Created by jpc on 5/30/17.
 */
import {Component, Input, OnInit} from '@angular/core';

@Component({
    selector: 'position',
    template: `
    <span>{{display}}</span>
    `,
    styles:[]
})
export class PositionComponent implements OnInit {
    @Input()
    value: Pos;
    display: string;


    ngOnInit(): void {
        console.info('value: ', JSON.stringify(this.value));
        this.display = JSON.stringify(this.value);
    }
}
