import {Box} from '@mantine/core'
import {FC, useContext, useEffect, useState} from 'react'
import {IPublishParams, useStompClient, useSubscription} from 'react-stomp-hooks'
import {Authentication, Bank} from '../entities.ts'
import {AuthenticationContext} from '../App.tsx'
import api from '../api.ts'

const SignedInView: FC = () => {
  const authentication = useContext<Authentication>(AuthenticationContext)
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
        setBanks(await api.banks(authentication.t.id_token))
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
