/**
 * Created by jpc on 5/29/17.
 */
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {Http} from "@angular/http";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

@Injectable()
export class ClsbService {
    data: string;
    base: string;

    constructor(private http: Http){
        this.data = 'coucou jp';
        let w: Window = <Window> window;
        let cs: string[] = (parseInt(w.location.port) >= 63342 ? ['http://', w.location.hostname, ':8080'] : [w.location.protocol, '://', w.location.host]);
        this.base = cs.concat(['/clsb', '/api']).join('');
        console.info('base:', this.base);
    }

    getPositions(): Observable<Map<string, Pos>> {
        return this.http.get(this.base + "/positions").map(r => <Map<string, Pos>> r.json());
    }

    getCurrencies(): Observable<Currency[]> {
        return this.http.get(this.base + "/currencies").map(r => <Currency[]> r.json());
    }

    getBank(): Observable<Bank> {
        return this.http.get(this.base + "/bank").map(r => <Bank> r.json());
    }

    sendCommand(cmd: string): Observable<string> {
        return this.http.get(this.base + "/command/" + cmd).map(r => <string> r.json());
    }


}
