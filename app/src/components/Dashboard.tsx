import {Text, Stack} from '@mantine/core'
import {FC, useContext} from 'react'
import {PerpetualContext} from './Admin.tsx'
import {IPublishParams, useStompClient, useSubscription} from 'react-stomp-hooks'
import {applicationState} from '../store.ts'
import CurrencyImage from './CurrencyImage.tsx'

const Dashboard: FC = () => {
  const perpetual = useContext(PerpetualContext)
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
    applicationState.notifyError('No stomp client')
  }
  useSubscription('/topic/ping', message => {
    console.log(`Received message: ${message.body}`)
  })

  return (
    <Stack>
      <Text>Admin dashboard</Text>
      <Text>{JSON.stringify(perpetual)}</Text>
      {perpetual.currencies.map(it =>
        <CurrencyImage c="red" ccy={it}/>
      )}
    </Stack>
  )
}

export default Dashboard
