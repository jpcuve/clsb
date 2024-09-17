import {Group, Select, Stack} from '@mantine/core'
import {FC, useEffect, useState} from 'react'
import {IPublishParams, useStompClient, useSubscription} from 'react-stomp-hooks'
import {Bank} from '../entities.ts'
import useClient from '../hooks/useClient.ts'
import AccountListView from './AccountListView.tsx'

const SignedInView: FC = () => {
  const client = useClient()
  const [banks, setBanks] = useState<Bank[]>([])
  const [selection, setSelection] = useState<number>(0)
  const handleChangeBank = (idAsString: string|null) => {
    setSelection(banks.findIndex(it => it.id.toString() === idAsString))
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
        <Select value={banks[selection].id.toString()} onChange={handleChangeBank} data={banks.map(it => ({value: it.id.toString(), label: it.denomination}))}/>
      </Group>
      {banks.length > 0 && <AccountListView bank={banks[selection]}/>}
    </Stack>
  )
}

export default SignedInView
