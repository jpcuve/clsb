export interface Authentication {
  t: {
    access_token: string,
    id_token: string,

  },
  u: {
    email: string,
    name: string,
    roles: string[],
    features: string[],
    aspects: string[],
  }
}

export interface Feedback {
  level: 'error'|'success'|'warning'|'info'
  message: string,
}

export interface Bank {
  id: number,
  denomination: string,
  baseIso: string,
}

export const defaultBank: Bank = {
  id: 0,
  denomination: '',
  baseIso: '',
}

export interface Account {
  id: number;
  denomination: string,
}

export interface Currency {
  id: number,
  iso: string,
}

export interface Perpetual {
  bank: Bank,
  currencies: Currency[],
  accounts: Account[],
}

export const defaultPerpetual: Perpetual = {
  bank: defaultBank,
  currencies: [],
  accounts: [],
}
