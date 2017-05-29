/**
 * Created by jpc on 5/29/17.
 */
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {Http, Response} from "@angular/http";

@Injectable()
export class ClsbService {
    data: String;

    constructor(private http: Http){
        this.data = 'coucou jp';
    }

    print(): void {
        console.info('message', this.data);
    }

    getPositions(): Observable<Response> {
        return this.http.get("http://localhost:8080/clsb/api/positions")

    }
}
