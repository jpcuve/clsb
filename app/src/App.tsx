import {Text, Stack, Button, Group} from '@mantine/core'
import {createContext, useEffect, useState} from 'react'
import SignedOutView from './components/SignedOutView.tsx'
import SignedInView from './components/SignedInView.tsx'
import {Authentication, defaultAuthentication} from './entities.ts'
import {navigate} from 'wouter/use-browser-location'
import {Route} from 'wouter'
import ErrorView from './components/ErrorView.tsx'
import api from './api.ts'

export const AuthenticationContext = createContext<Authentication>(defaultAuthentication)

function App() {
  console.log(`Starting: ${import.meta.env.VITE_APP_TITLE}`)
  const [authentication, setAuthentication] = useState<Authentication>()
  const signInOut = () => {
    if (authentication){
      setAuthentication(undefined)
      navigate('/')
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
          navigate('/secure')
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
      <Route path="/"><SignedOutView/></Route>
      {authentication && <Route path="/secure">
          <AuthenticationContext.Provider value={authentication}>
              <SignedInView/>
          </AuthenticationContext.Provider>
      </Route>}
      <Route path="/error" component={ErrorView}/>
    </Stack>
  )
}

export default App
