/**
 * Created by jpc on 5/9/17.
 */
import {Injectable} from "@angular/core";

@Injectable()
export class SomeService {
    private data: String;

    public constructor(){
        this.data = 'coucou jp';
    }

    public print(): void {
        console.info('message', this.data);
    }
}

