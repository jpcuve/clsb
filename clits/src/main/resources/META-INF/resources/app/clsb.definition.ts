/**
 * Created by jpc on 29-05-17.
 */
interface Pos {
    [currency: string]: number;
}

interface Currency {
    iso: string;
    opening: string;
    fundingCompletionTarget: string;
    closing: string;
}

interface Bank {
    opening: string;
    settlementCompletionTarget: string;
    closing: string;
}