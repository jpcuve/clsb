/**
 * Created by jpc on 5/29/17.
 */
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {Http, Response} from "@angular/http";
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

@Injectable()
export class ClsbService {
    data: String;

    constructor(private http: Http){
        this.data = 'coucou jp';
    }

    print(): void {
        console.info('message', this.data);
    }

    getPositions(): Observable<Map<string, any>> {
        return this.http.get("http://localhost:8080/clsb/api/positions").map(r => <Map<string, any>>r.json());

    }
}
