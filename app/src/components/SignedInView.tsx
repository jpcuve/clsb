import {Box} from '@mantine/core'
import {FC} from 'react'
import {useStompClient} from 'react-stomp-hooks'
import {Authentication} from '../entities.ts'

const SignedInView: FC<{authentication: Authentication}> = props => {
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
    <Box>Signed in view: {JSON.stringify(props.authentication)}</Box>
  )
}

export default SignedInView
