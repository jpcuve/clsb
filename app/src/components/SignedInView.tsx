import {Group, Select, Stack} from '@mantine/core'
import {FC, useEffect, useState} from 'react'
import {IPublishParams, useStompClient, useSubscription} from 'react-stomp-hooks'
import {Bank} from '../entities.ts'
import useClient from '../hooks/useClient.ts'
import {Route} from 'wouter'
import {navigate} from 'wouter/use-browser-location'
import DashboardView from './DashboardView.tsx'

const SignedInView: FC = () => {
  const client = useClient()
  const [banks, setBanks] = useState<Bank[]>([])
  const [bank, setBank] = useState<Bank|undefined>()
  const handleChangeBank = (idAsString: string|null) => {
    if (idAsString){
      setBank(banks.find(it => it.id.toString() === idAsString))
      navigate(`${import.meta.env.VITE_APP_WEB_CONTEXT}/secure/dashboard`)
    }
  }
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
    <Stack>
      <Group>
        <Select value={bank?.id?.toString() ?? '0'} onChange={handleChangeBank} data={banks.map(it => ({value: it.id.toString(), label: it.denomination}))}/>
      </Group>
      {bank && <Route path="dashboard">
          <DashboardView bank={bank}/>
      </Route>
      }
    </Stack>
  )
}

export default SignedInView
