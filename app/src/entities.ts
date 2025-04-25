import {today} from './helper.ts'

export interface Token {
  access_token: string,
  id_token: string,
  refresh_token?: string,
}

export interface PasswordPolicy {
  id?: number,
  emailPattern: string,
  rank: number,
  minLength: number,
  maxLength: number,
  minLowerCase: number,
  minUpperCase: number,
  minDigit: number,
  minSpecial: number,
  minAgeInDays: number,
  maxAgeInDays: number,
  maxPastPasswordCount: number,
  maxFailedSignInAttemptCount: number,
  sessionTimeoutInSeconds: number,
}

export const defaultPasswordPolicy: PasswordPolicy = {
  emailPattern: '.*',
  rank: 1,
  minLength: 8,
  maxLength: 64,
  minLowerCase: 0,
  minUpperCase: 0,
  minDigit: 0,
  minSpecial: 0,
  minAgeInDays: 0,
  maxAgeInDays: 0,
  maxPastPasswordCount: 8,
  maxFailedSignInAttemptCount: 6,
  sessionTimeoutInSeconds: 0,
}

export interface Userinfo {
  email: string,
  name: string,
  roles: string[],
  features: string[],
  aspects: string[],
  password_policy: PasswordPolicy,
}

export interface Authentication {
  token: Token,
  userinfo: Userinfo
}

export type LocalTime = [number, number]

export interface Bank {
  id: number,
  denomination: string,
  baseIso: string,
  opening: LocalTime,
  closing: LocalTime,
  settlementCompletionTarget: LocalTime,
}

export const defaultBank: Bank = {
  id: 0,
  denomination: '',
  baseIso: '',
  opening: [0, 0],
  closing: [23, 59],
  settlementCompletionTarget: [0, 0],
}

export interface Account {
  id: number;
  denomination: string,
}

export interface Currency {
  color: string,
  id: number,
  iso: string,
  opening: LocalTime,
  fundingCompletionTarget: LocalTime,
  closing: LocalTime,
  close: LocalTime,
  realTimeGrossSettlementPeriods: {init: LocalTime, done: LocalTime}[],
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
