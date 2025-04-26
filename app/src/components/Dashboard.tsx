import {Stack} from '@mantine/core'
import {FC} from 'react'
import {IPublishParams, useStompClient, useSubscription} from 'react-stomp-hooks'
import {notifications} from "@mantine/notifications";

const Dashboard: FC = () => {
  const stompClient = useStompClient()
  if (stompClient){
    const message: IPublishParams = {
      destination: '/app/echo',
      body: 'Echo 123',
      headers: {'id-token': ''},
    }
    console.log(`Sending message: ${message.body}`)
    stompClient.publish(message)
  } else {
    notifications.show({color: 'red', message: 'No stomp client'})
  }
  useSubscription('/topic/ping', message => {
    console.log(`Received message: ${message.body}`)
  })

  return (
    <Stack>
      Dashboard
    </Stack>
  )
}

export default Dashboard
