import {Component, Input} from '@angular/core';

@Component({
    selector: 'position',
    template: `
    <span *ngForOf="let (ccy,amount) of position">
        <span style="font-size: small;">{{ccy}}</span>
        <span>{{amount}}</span>
    </span>
    `
})
export class PositionComponent {
    @Input() position: Position;
}
