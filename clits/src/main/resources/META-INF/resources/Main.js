/**
 * Created by jpc on 02-05-17.
 */
function main() {
    console.log('coucou');
    var c1 = new Complex(1, 2);
    var c2 = c1.add(new Complex(3, 4));
    return c2.toString();
}
window.document.body.innerHTML = main();
