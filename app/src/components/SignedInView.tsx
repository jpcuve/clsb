import {Box} from '@mantine/core'
import {FC, useEffect, useState} from 'react'
import {IPublishParams, useStompClient, useSubscription} from 'react-stomp-hooks'
import {Authentication, Bank} from '../entities.ts'
import api from '../api.ts'
import {useSessionStorage} from 'usehooks-ts'

const SignedInView: FC = () => {
  const [authentication] = useSessionStorage<Authentication|undefined>(import.meta.env.VITE_APP_WEB_CONTEXT, undefined)
  console.log(`Authentication: ${JSON.stringify(authentication)}`)
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
    if (authentication){
      (async () => {
        try {
          setBanks(await api.banks(authentication.t.id_token))
        } catch (e: any){
          console.log(e.message)
        }
      })()
    }
  }, [])
  return (
    <Box>{JSON.stringify(banks)}</Box>
  )
}

export default SignedInView
