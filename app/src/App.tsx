import {Text, Stack, Button, Group} from '@mantine/core'
import {useEffect} from 'react'
import SignedOutView from './components/SignedOutView.tsx'
import SignedInView from './components/SignedInView.tsx'
import {Authentication} from './entities.ts'
import {navigate} from 'wouter/use-browser-location'
import {Route, Router} from 'wouter'
import ErrorView from './components/ErrorView.tsx'
import api from './api.ts'
import {useSessionStorage} from 'usehooks-ts'

function App() {
  console.log(`Starting: ${import.meta.env.VITE_APP_TITLE}`)
  const webContext = import.meta.env.VITE_APP_WEB_CONTEXT
  const [authentication, setAuthentication, removeAuthentication] = useSessionStorage<Authentication|undefined>(webContext, undefined)
  const signInOut = () => {
    if (authentication){
      removeAuthentication()
      navigate(import.meta.env.VITE_APP_WEB_CONTEXT)
    } else {
      const search = new URLSearchParams()
      search.append('client_id', import.meta.env.VITE_APP_CLIENT_ID)
      search.append('redirect_uri', `${window.location.protocol}//${window.location.host}${import.meta.env.VITE_APP_WEB_CONTEXT}`)
      search.append('scope', 'openid email profile')
      search.append('response_type', 'code')
      window.location.replace(`${import.meta.env.VITE_APP_IDENTITY_URL}/sign-in?${search}`)
    }
  }
  const handleError = (message: string) => navigate(`/error?${message}`)
  useEffect(() => {
    (async () => {
      const urlParams = new URLSearchParams(window.location.search)
      const code = urlParams.get('code')
      if (!authentication && code){
        try {
          const t = await api.token(code, urlParams.get('scope') || '')
          if (t.error) {
            handleError(t.error)
          }
          const u = await api.userInfo(t.access_token)
          if (u.error){
            handleError(u.error)
          }
          setAuthentication({t, u})
          navigate(`${import.meta.env.VITE_APP_WEB_CONTEXT}/secure`)
        } catch(e: any){
          handleError(e.message)
        }
      }
    })()
  }, [])
  return (
    <Stack p="sm">
      <Group>
        {authentication && <Text>{authentication.u.name}</Text>}
        <Button onClick={signInOut}>{authentication ? 'Sign-out' : 'Sign-in'}</Button>
      </Group>
      <Router base={import.meta.env.VITE_APP_WEB_CONTEXT}>
        <Route path="/"><SignedOutView/></Route>
        {authentication && <Route path="/secure">
          <SignedInView/>
        </Route>}
        <Route path="/error" component={ErrorView}/>
      </Router>
    </Stack>
  )
}

export default App
