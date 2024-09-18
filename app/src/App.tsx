import {Text, Stack, Button, Group} from '@mantine/core'
import {useEffect} from 'react'
import {Authentication} from './entities.ts'
import {useSessionStorage} from 'usehooks-ts'
import {useNavigate} from 'react-router'
import {Outlet} from 'react-router-dom'

const fetchToken = async (code: string, scope: string) => {
  const search = new URLSearchParams()
  search.append('grant_type', 'authorization_code')
  search.append('code', code)
  search.append('redirect_uri', `${window.location.protocol}//${window.location.host}${window.location.pathname}`)
  search.append('scope', scope)
  const res = await fetch(`${import.meta.env.VITE_APP_IDENTITY_URL}/auth/token`,  {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    body: search,
  })
  return res.json()
}

const fetchUserInfo = async (token: string) => {
  const res = await fetch(`${import.meta.env.VITE_APP_IDENTITY_URL}/auth/userinfo`, {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  })
  return res.json()
}


function App() {
  console.log(`Starting: ${import.meta.env.VITE_APP_TITLE}`)
  const navigate = useNavigate()
  const webContext = import.meta.env.VITE_APP_WEB_CONTEXT
  const [authentication, setAuthentication, removeAuthentication] = useSessionStorage<Authentication|undefined>(webContext, undefined)
  const signInOut = () => {
    if (authentication){
      removeAuthentication()
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
  const handleError = (message: string) => navigate(`${import.meta.env.VITE_APP_WEB_CONTEXT}/error?${message}`)
  useEffect(() => {
    (async () => {
      const urlParams = new URLSearchParams(window.location.search)
      const code = urlParams.get('code')
      if (!authentication && code){
        try {
          const t = await fetchToken(code, urlParams.get('scope') || '')
          if (t.error) {
            handleError(t.error)
          }
          const u = await fetchUserInfo(t.access_token)
          if (u.error){
            handleError(u.error)
          }
          setAuthentication({t, u})
          navigate('admin/dashboard')
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
      <Outlet/>
{/*
      <Router basename={import.meta.env.VITE_APP_WEB_CONTEXT}>
        <Routes>
          <Route path="/"><SignedOutView/></Route>
          {authentication && <Route path="/secure">
              <SignedInView/>
          </Route>}
          <Route path="/error" component={ErrorView}/>
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Router>
*/}
    </Stack>
  )
}

export default App
