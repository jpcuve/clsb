import './App.css'
import {AppShell, Button, Container } from '@mantine/core'
import {useStompClient} from 'react-stomp-hooks'
import {useState} from 'react'

function App() {
  console.log(`Starting: ${import.meta.env.VITE_APP_TITLE}`)
  const [signedIn, isSignedIn] = useState<boolean>(false)
  if (!signedIn){
    const location = window.location
    const uri = `${location.protocol}//${location.host}${location.pathname}`
    console.log(`uri: ${uri}`)
    const urlParams = new URLSearchParams(location.search)
    const code = urlParams.get('code')
    if (code){
      // processing URL with code
    } else {
      // processing URL without code
    }
  }
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
  return (
    <AppShell header={{ height: 60 }}>
      <AppShell.Header>
        Header
      </AppShell.Header>
      <AppShell.Navbar p="md">
        Navbar
      </AppShell.Navbar>
      <AppShell.Main>
        <Container>
          <Button>Test</Button>
        </Container>
      </AppShell.Main>
      <AppShell.Footer>
        Footer
      </AppShell.Footer>
    </AppShell>
  )
}

export default App
