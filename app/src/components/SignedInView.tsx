import {Box} from '@mantine/core'
import {FC, useEffect, useState} from 'react'
import {IPublishParams, useStompClient, useSubscription} from 'react-stomp-hooks'
import {Bank} from '../entities.ts'
import useClient from '../hooks/useClient.ts'

const SignedInView: FC = () => {
  const client = useClient()
  const [banks, setBanks] = useState<Bank[]>([])
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
  useEffect(() => {
    (async () => {
      try {
        setBanks(await client.banks())
      } catch (e: any){
        console.log(e.message)
      }
    })()
  }, [])
  return (
    <Box>{JSON.stringify(banks)}</Box>
  )
}

export default SignedInView
