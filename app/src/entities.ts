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

export const defaultAuthentication: Authentication = {
  t: {
    access_token: '',
    id_token: '',
  },
  u: {
    email: '',
    name: '',
    roles: [],
    features: [],
    aspects: [],
  }
}