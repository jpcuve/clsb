/**
 * Created by jpc on 02-05-17.
 */

class Complex {
    private _re: number;
    private _im: number;

    constructor(re: number, im: number){
        this._re = re;
        this._im = im;
    }

    add(c: Complex): Complex{
        return new Complex(this._re + c._re, this._im + c._im);
    }

    toString(): string {
        return [this._re, "+i ", this._im].join("");
    }
}

