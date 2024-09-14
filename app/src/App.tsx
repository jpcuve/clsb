import './App.css'
import {Text, Box, Stack, Button} from '@mantine/core'
import {useEffect, useState} from 'react'
import SignedOutView from './components/SignedOutView.tsx'
import SignedInView from './components/SignedInView.tsx'
import {Authentication, defaultAuthentication} from './entities.ts'


function App() {
  console.log(`Starting: ${import.meta.env.VITE_APP_TITLE}`)
  const uri = `${location.protocol}//${location.host}${location.pathname}`
  const urlParams = new URLSearchParams(location.search)
  const [signInUrl, setSignInUrl] = useState<string>('')
  const [error, setError] = useState<string|null>(urlParams.get('error'))
  const [authentication, setAuthentication] = useState<Authentication>()
  const signOut = () => {
    setAuthentication(undefined)
    window.location.href = uri
  }
  useEffect(() => {
    (async () => {
      if (!authentication){
        const code = urlParams.get('code')
        if (code){
          const search = new URLSearchParams()
          search.append('grant_type', 'authorization_code')
          search.append('code', code)
          search.append('redirect_uri', uri)
          search.append('scope', urlParams.get('scope') || '')
          try {
            const res = await fetch(`${import.meta.env.VITE_APP_IDENTITY_URL}/auth/token`,  {
              method: 'POST',
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
              },
              body: search,
            })

            const t = await res.json()
            const {error, access_token} = t
            if (error){
              setError(error)
            } else {
              let authentication: Authentication = {...defaultAuthentication, t}
              const res = await fetch(`${import.meta.env.VITE_APP_IDENTITY_URL}/auth/userinfo`, {
                headers: {
                  'Authorization': `Bearer ${access_token}`
                }
              })
              authentication.u = await res.json()
              setAuthentication(authentication)
            }
          } catch(e: any){
            setError(e.message)
          }
        } else {
          console.log(`uri: ${uri}`)
          const search = new URLSearchParams()
          search.append('client_id', import.meta.env.VITE_APP_CLIENT_ID)
          search.append('redirect_uri', uri)
          search.append('scope', 'openid email profile')
          search.append('response_type', 'code')
          setSignInUrl(`${import.meta.env.VITE_APP_IDENTITY_URL}/sign-in?${search}`)
        }
      }
    })()
  }, [])
  return (
    <Box>
      {!authentication && <Stack>
          <Text>Signed out</Text>
          <Button onClick={() => window.location.replace(signInUrl)}>Sign-in</Button>
          <SignedOutView/>
      </Stack>}
      {authentication && <Stack>
          <Text>Signed in</Text>
          <Button onClick={signOut}>Sign-out</Button>
          <SignedInView authentication={authentication}/>
      </Stack>}
      {error && <Text c="red">{error}</Text>}
    </Box>
  )
}

export default App
