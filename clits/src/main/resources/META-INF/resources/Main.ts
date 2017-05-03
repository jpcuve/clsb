/**
 * Created by jpc on 02-05-17.
 */

function main(): string{
    console.log('coucou');
    let c1 = new Complex(1, 2);
    let c2 = c1.add(new Complex(3, 4));
    return c2.toString();
}

window.document.body.innerHTML = main();