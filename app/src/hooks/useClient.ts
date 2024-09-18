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
  class Client {
    async banks() { return await (await fetch(`${import.meta.env.VITE_APP_REMOTE_HOST}${import.meta.env.VITE_APP_WEB_CONTEXT}/api/banks`, {headers})).json() as Promise<Bank[]>}
    async perpetual(bankId: number){ return await (await fetch(`${import.meta.env.VITE_APP_REMOTE_HOST}${import.meta.env.VITE_APP_WEB_CONTEXT}/api/perpetual/${bankId}`, {headers})).json() as Promise<Perpetual>}
  }
  return new Client()
}

export default useClient
