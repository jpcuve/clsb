/**
 * Created by jpc on 02-05-17.
 */
var Complex = (function () {
    function Complex(re, im) {
        this._re = re;
        this._im = im;
    }
    Complex.prototype.add = function (c) {
        return new Complex(this._re + c._re, this._im + c._im);
    };
    Complex.prototype.toString = function () {
        return [this._re, "+i ", this._im].join(",");
    };
    return Complex;
}());
