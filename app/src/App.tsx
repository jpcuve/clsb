import {Text, Stack, Button, Group} from '@mantine/core'
import {useEffect, useState} from 'react'
import SignedOutView from './components/SignedOutView.tsx'
import SignedInView from './components/SignedInView.tsx'
import {Authentication} from './entities.ts'
import {navigate} from 'wouter/use-browser-location'
import {Route} from 'wouter'
import ErrorView from './components/ErrorView.tsx'


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
      search.append('redirect_uri', `${window.location.protocol}//${window.location.host}`)  // TODO add deploy web context
      search.append('scope', 'openid email profile')
      search.append('response_type', 'code')
      window.location.replace(`${import.meta.env.VITE_APP_IDENTITY_URL}/sign-in?${search}`)
    }
  }
  useEffect(() => {
    (async () => {
      const urlParams = new URLSearchParams(window.location.search)
      const code = urlParams.get('code')
      if (!authentication && code){
        const search = new URLSearchParams()
        search.append('grant_type', 'authorization_code')
        search.append('code', code)
        search.append('redirect_uri', `${window.location.protocol}//${window.location.host}${window.location.pathname}`)
        search.append('scope', urlParams.get('scope') || '')
        try {
          let res = await fetch(`${import.meta.env.VITE_APP_IDENTITY_URL}/auth/token`,  {
            method: 'POST',
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: search,
          })
          const t = await res.json()
          if (t.error) {
            throw new Error(t.error)
          }
          res = await fetch(`${import.meta.env.VITE_APP_IDENTITY_URL}/auth/userinfo`, {
            headers: {
              'Authorization': `Bearer ${t.access_token}`
            }
          })
          const u = await res.json()
          if (u.error){
            throw new Error(u.error)
          }
          setAuthentication({t, u})
          navigate('/secure')
        } catch(e: any){
          navigate(`/error?${e.message}`)
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
      {authentication && <Route path="/secure"><SignedInView authentication={authentication}/></Route>}
      <Route path="/error" component={ErrorView}/>
    </Stack>
  )
}

export default App
