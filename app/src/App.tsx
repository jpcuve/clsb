import './App.css'
import {AppShell, Button, Container } from '@mantine/core'
import {useStompClient} from 'react-stomp-hooks'

function App() {
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
