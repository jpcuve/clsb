import {Bank, Perpetual, Token, Userinfo} from './entities.ts'

class SecureClient {
  private accessTokenKey = `${import.meta.env.VITE_APP_CLIENT_ID}_access_token`
  private idTokenKey = `${import.meta.env.VITE_APP_CLIENT_ID}_id_token`
  private refreshTokenKey = `${import.meta.env.VITE_APP_CLIENT_ID}_refresh_token`
  private userinfoKey = `${import.meta.env.VITE_APP_CLIENT_ID}_userinfo`

  private async fetchToken(body: URLSearchParams){
    const res = await fetch(`${import.meta.env.VITE_APP_IDENTITY_URL}/auth/token`, {method: 'POST', body})
    if (res.ok){
      const json = await res.json() as Token
      sessionStorage.setItem(this.idTokenKey, json.id_token)
      sessionStorage.setItem(this.accessTokenKey, json.access_token)
      if (json.refresh_token){
        localStorage.setItem(this.refreshTokenKey, json.refresh_token)
      }
    }
  }

  private async fetchUserinfo(){
    const res = await fetch(`${import.meta.env.VITE_APP_IDENTITY_URL}/auth/userinfo`, {headers: {Authorization: `Bearer ${sessionStorage.getItem(this.accessTokenKey)}`}})
    const userinfo = await res.json() as Userinfo
    console.log(`Userinfo: ${JSON.stringify(userinfo)}`)
    sessionStorage.setItem(this.userinfoKey, JSON.stringify(userinfo))
  }

  get idToken(): string {
    return sessionStorage.getItem(this.idTokenKey) ?? ''
  }

  get signedIn(): boolean {
    return this.idToken.length > 0
  }

  get userinfo(): Userinfo {
    return JSON.parse(sessionStorage.getItem(this.userinfoKey) ?? '{}') as Userinfo
  }

  get isTeacher(): boolean {
    return this.userinfo.roles?.includes('teacher') ?? false
  }

  get isAdministrator(): boolean {
    return this.userinfo.features?.includes('administrator') ?? false
  }

  async refresh(): Promise<boolean>{
    sessionStorage.removeItem(this.accessTokenKey)
    sessionStorage.removeItem(this.idTokenKey)
    sessionStorage.removeItem(this.userinfoKey)
    const refreshToken = localStorage.getItem(this.refreshTokenKey)
    if (!refreshToken){
      return false
    }
    const body   = new URLSearchParams()
    body.append('grant_type', 'refresh_token')
    body.append('refresh_token', refreshToken)
    await this.fetchToken(body)
    await this.fetchUserinfo()
    return true
  }

  authorizeSearchParameters(scope: string): URLSearchParams {
    const searchParams = new URLSearchParams()
    searchParams.append('response_type', 'code')
    searchParams.append('client_id', import.meta.env.VITE_APP_CLIENT_ID)
    searchParams.append('redirect_uri', `${window.location.protocol}//${window.location.host}${import.meta.env.VITE_APP_WEB_CONTEXT}`)
    searchParams.append('scope', scope)
    return searchParams
  }

  async signIn(authorizationCode: string, scope: string){
    const body   = new URLSearchParams()
    body.append('grant_type', 'authorization_code')
    body.append('code', authorizationCode)
    body.append('redirect_uri', `${window.location.protocol}//${window.location.host}${import.meta.env.VITE_APP_WEB_CONTEXT}`)
    body.append('scope', scope)
    await this.fetchToken(body)
    await this.fetchUserinfo()
  }

  signOut(){
    sessionStorage.removeItem(this.accessTokenKey)
    sessionStorage.removeItem(this.idTokenKey)
    localStorage.removeItem(this.refreshTokenKey)
    sessionStorage.removeItem(this.userinfoKey)
  }

  private async rpc<T>(endPoint: string, method: string = 'GET', contentType: string|undefined = undefined, body: BodyInit|undefined = undefined): Promise<T> {
    const headers: Record<string, string> = {
      'Accept': 'application/json',
      'Credentials': 'include',
    }
    if (contentType){
      headers['Content-Type'] = contentType
    }
    let idToken = sessionStorage.getItem(this.idTokenKey)
    if (idToken){
      headers['Authorization'] = `Bearer ${idToken}`
    } else {
      console.error(`Error: idToken not found`)
    }
    const options: RequestInit = {
      method,
      headers,
      body,
    }
    let res = await fetch(`${import.meta.env.VITE_APP_REMOTE_HOST}${import.meta.env.VITE_APP_WEB_CONTEXT}${endPoint}`, options)
    if (!res.ok){
      if (res.status === 401 || res.status === 403){
        // attempt to refresh
        await this.refresh()
        idToken = sessionStorage.getItem(this.idTokenKey)
        console.log(`Refreshing, new id token: ${idToken}`)
        res = await fetch(`${import.meta.env.VITE_APP_REMOTE_HOST}${import.meta.env.VITE_APP_WEB_CONTEXT}${endPoint}`, {...options, headers: {...options.headers, 'Authorization': `Bearer ${idToken}`}})
        if (!res.ok)  {
          throw Error('Unauthorized')
        }
      } else {
        throw Error(`Error: ${res.status}`)
      }
    }
    const json = await res.json()
    if (typeof json === 'object' && 'error' in json) {
      const message = json.error
      console.error(json['error_description'])
      throw Error(message)
    }
    return json as T;
  }

  private async get<T>(endPoint: string): Promise<T> {
    return this.rpc<T>(`/api/${endPoint}`)
  }

  private async post<T>(endPoint: string, data: unknown): Promise<T> {
    let contentType: undefined|string
    if (data instanceof FormData){
      contentType = undefined
    } else if (data instanceof URLSearchParams) {
      contentType = 'application/x-www-form-urlencoded'
    } else if (typeof data === 'string') {
      contentType = 'text/plain'
    } else {
      contentType = 'application/json'
      data = JSON.stringify(data)
    }
    return this.rpc<T>(`/api/${endPoint}`, 'POST', contentType, data as XMLHttpRequestBodyInit)
  }

/*
  private async put<T>(endPoint: string, data: unknown): Promise<T> {
    return this.rpc<T>(`/api/${endPoint}`, 'PUT', 'application/json', JSON.stringify(data))
  }
*/

  private async delete<T>(endPoint: string, data: unknown): Promise<T> {
    return this.rpc<T>(`/api/${endPoint}`, 'DELETE', 'application/json', JSON.stringify(data))
  }

  async status() { return await this.get<unknown>('status') }
  async perpetual(bankId: number) { return await this.get<Perpetual>(`perpetual/${bankId}`) }
  async banks() { return await this.get<Bank[]>(`banks`) }
  async ids(entity: string, query: unknown){return await this.post<number[]>(`ids/${entity}`, query)}
  async entities<T>(entity: string, ids: number[]) { return await this.post<T[]>(`entities/${entity}`, ids) }
  async deleteIds(entity: string, ids: number[]){ return await this.delete<boolean>(entity, ids) }
  async save<T>(entity: string, t: T) { return await this.post<T>(entity, t) }
}

const secureClient = new SecureClient()

export default secureClient
