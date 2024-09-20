import {useSessionStorage} from 'usehooks-ts'
import {Authentication, Bank, Perpetual} from '../entities.ts'

function useClient(){
  const [authentication] = useSessionStorage<Authentication|undefined>(import.meta.env.VITE_APP_WEB_CONTEXT, undefined)
  const headers: HeadersInit = {
    'Accept': 'application/json'
  }
  if (authentication){
    headers['Authorization'] = `Bearer ${authentication.t.id_token}`
  }
  const endPoint: string = `${import.meta.env.VITE_APP_REMOTE_HOST}${import.meta.env.VITE_APP_WEB_CONTEXT}/api`
  class Client {
    async banks() { return await (await fetch(`${endPoint}/banks`, {headers})).json() as Promise<Bank[]>}
    async perpetual(bankId: number){ return await (await fetch(`${endPoint}/perpetual/${bankId}`, {headers})).json() as Promise<Perpetual>}
  }
  return new Client()
}

export default useClient
