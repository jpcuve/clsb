import {Box} from '@mantine/core'
import {FC} from 'react'
import {IPublishParams, useStompClient, useSubscription} from 'react-stomp-hooks'
import {Authentication} from '../entities.ts'

const SignedInView: FC<{authentication: Authentication}> = props => {
  const stompClient = useStompClient()
  if (stompClient){
    const message: IPublishParams = {
      destination: "/app/echo",
      body: "Echo 123",
      headers: {'id-token': 'testing'}
    }
    console.log(`Sending message: ${message.body}`)
    stompClient.publish(message)
  } else {
    console.log("No stomp client available")
  }
  useSubscription('/topic/ping', message => {
    console.log(`Received message: ${message.body}`)
  })
  return (
    <Box>Signed in view: {JSON.stringify(props.authentication)}</Box>
  )
}

export default SignedInView
