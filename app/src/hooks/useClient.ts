import {useSessionStorage} from 'usehooks-ts'
import {Authentication, Bank} from '../entities.ts'

function useClient(){
  const [authentication] = useSessionStorage<Authentication|undefined>(import.meta.env.VITE_APP_WEB_CONTEXT, undefined)
  const headers: HeadersInit = {
    'Accept': 'application/json'
  }
  if (authentication){
    headers['Authorization'] = `Bearer ${authentication.t.id_token}`
  }
  const client: any = {
    banks: async () => await (await fetch(`${import.meta.env.VITE_APP_REMOTE_HOST}${import.meta.env.VITE_APP_WEB_CONTEXT}/api/banks`, {headers})).json() as Promise<Bank[]>
  }
  return client
}

export default useClient
