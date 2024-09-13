import './App.css'
import {Text, Box, Stack, Button} from '@mantine/core'
import {useStompClient} from 'react-stomp-hooks'
import {useEffect, useState} from 'react'

function App() {
  console.log(`Starting: ${import.meta.env.VITE_APP_TITLE}`)
  const uri = `${location.protocol}//${location.host}${location.pathname}`
  const urlParams = new URLSearchParams(location.search)
  const [signInUrl, setSignInUrl] = useState<string>('')
  const [error, setError] = useState<string|null>(urlParams.get('error'))
  const [authentication, setAuthentication] = useState<any>()
  const signOut = () => {
    setAuthentication(undefined)
    window.location.href = uri
  }
  useEffect(() => {
    (async () => {
      if (!authentication){
        const code = urlParams.get('code')
        if (code){
          // processing URL with code
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
              const res = await fetch(`${import.meta.env.VITE_APP_IDENTITY_URL}/auth/userinfo`, {
                headers: {
                  'Authorization': `Bearer ${access_token}`
                }
              })
              const u = await res.json()
              console.log(`User info: ${JSON.stringify(u)}`)
              setAuthentication({t, u})
            }
          } catch(e: any){
            setError(e.message)
          }
          // navigate('/')

        } else {
          // processing URL without code
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
/*
  const stompClient = useStompClient()
  if (stompClient){
    console.log("Sending stomp message")
    stompClient.publish({
      destination: "/app/echo",
      body: "Echo 123",
      headers: {'id-token': 'testing'}
    })
  } else {
    console.log("No stomp client available")
  }
*/
  return (
    <Box>
      {!authentication && <Stack>
          <Text>Signed out</Text>
          <Button onClick={() => window.location.replace(signInUrl)}>Sign-in</Button>
      </Stack>}
      {authentication && <Stack>
          <Text>Signed in</Text>
          <Button onClick={signOut}>Sign-out</Button>
          <Text fz="xs">{JSON.stringify(authentication)}</Text>
      </Stack>}
      {error && <Text c="red">{error}</Text>}
    </Box>
  )
}

export default App
