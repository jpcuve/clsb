import {useEffect, useState} from 'react'
import {Bank} from '../entities.ts'
import useClient from '../hooks/useClient.ts'
import {applicationState} from '../store.ts'

const Dashboard = () => {
  const client = useClient()
  const [banks, setBanks] = useState<Bank[]>([])
  useEffect(() => {
    (async () => {
      try {
        setBanks(await client.banks())
      } catch (e: any) {
        applicationState.notifyError(e.message)
      }
    })()
  }, [])
  return (
    <div>Dashboard: {JSON.stringify(banks)}</div>
  )
}

export default Dashboard
