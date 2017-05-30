import {Component, OnInit} from "@angular/core";
import {ClsbService} from "./clsb.service";

@Component({
    selector: 'overview',
    template: `
<svg [attr.viewBox]="['0 0 1440 ', (currencies.length + 2) * hu].join('')" version = "1.1" xmlns="http://www.w3.org/2000/svg">
    <line x1="0" y1="0" [attr.x2]="24 * wu" y2="0"/>
    <g *ngFor="let h of hours; let i = index">
        <line [attr.x1]="i * wu" y1="0" [attr.x2]="i * wu" [attr.y2]="hu"/>
        <text [attr.x]="i * wu + 2" y="10" class="text-small">{{i}}</text>
    </g>
    <g *ngIf="bank" transform="translate(0,20)">
        <rect [attr.x]="time(bank.opening)" y="0" [attr.width]="time(bank.closing) - time(bank.opening)" [attr.height]="hu" fill="aquamarine"/>
        <line [attr.x1]="time(bank.settlementCompletionTarget)" y1="0" [attr.x2]="time(bank.settlementCompletionTarget)" [attr.y2]="hu" class="sct"/>
    </g>
    <g *ngFor="let c of currencies; let i = index;" [attr.transform]="['translate(0,', (i + 2) * hu,')'].join('')">
        <rect [attr.x]="time(c.opening)" y="0" [attr.width]="time(c.closing) - time(c.opening)" [attr.height]="hu" fill="pink"/>
        <text [attr.x]="time(c.opening)" [attr.y]="hu">{{c.iso}}</text>
        <line [attr.x1]="time(c.fundingCompletionTarget)" y1="0" [attr.x2]="time(c.fundingCompletionTarget)" [attr.y2]="hu" class="fct"/>
        <line [attr.x1]="time(c.close)" y1="0" [attr.x2]="time(c.close)" [attr.y2]="hu" class="cc"/>
    </g>
    <line [attr.x1]="time(now)" y1="0" [attr.x2]="time(now)" [attr.y2]="(currencies.length + 2) * hu" class="current"/>

</svg>
`,
    styles:[
        'line { stroke: black; stroke-width: 1px; vector-effect: non-scaling-stroke; }',
        'text { font-family: sans-serif; font-size: 20px; }',
        '.text-small { font-size: 10px; }',
        '.sct { stroke: black; }',
        '.fct { stroke: blue; }',
        '.cc { stroke: red; }',
        '.current { stroke-width: 5; stroke-dasharray: 5; }'
    ]
})
export class OverviewComponent implements OnInit {
    wu: number = 60;
    hu: number = 20;
    hours: number[] = [];
    currencies: Currency[] = [];
    bank: Bank;
    now: string = '10:15';

    constructor(private service: ClsbService){
        for (let i = 0; i < 24; i++){
            this.hours.push(i);
        }
    }

    time(t: string): number{
        if (t){
            let colon = t.indexOf(":");
            if (colon >= 0){
                return parseInt(t.substring(0, colon)) * this.wu + parseInt(t.substring(colon + 1));
            }
        }
        return 0;
    };

    ngOnInit(): void {
        this.service.getCurrencies().subscribe(
            o => {
                this.currencies = o;
            }
        );
        this.service.getBank().subscribe(
            o => {
                console.info('bank', JSON.stringify(o));
                this.bank = o;
            }
        );
    }
}

