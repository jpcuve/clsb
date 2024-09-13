import {Box} from '@mantine/core'
import {FC} from 'react'
import {useStompClient} from 'react-stomp-hooks'

const SignedInView: FC = () => {
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
    <Box>Signed in view</Box>
  )
}

export default SignedInView
