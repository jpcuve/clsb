import {createContext, useEffect, useState} from 'react'
import {Bank, defaultPerpetual, Perpetual} from '../entities.ts'
import useClient from '../hooks/useClient.ts'
import {applicationState} from '../store.ts'
import {Group, Select, Stack, Text} from '@mantine/core'
import {Outlet} from 'react-router-dom'

export const PerpetualContext = createContext<Perpetual>(defaultPerpetual)

const Admin = () => {
  const client = useClient()
  const [banks, setBanks] = useState<Bank[]>([])
  const [perpetual, setPerpetual] = useState<Perpetual>(defaultPerpetual)
  const handleChangeBank = async (idAsString: string|null) => {
    if (idAsString){
      const bankId = Number(idAsString)
      const perpetual = await client.perpetual(bankId)
      setPerpetual(perpetual)
      applicationState.updatePerpetual(perpetual)
    }
  }
  useEffect(() => {
    (async () => {
      try {
        const banks = await client.banks()
        setBanks(banks)
        if (banks.length > 0){
          await handleChangeBank(banks[0].id.toString())
        }
      } catch (e: any) {
        applicationState.notifyError(e.message)
      }
    })()
  }, [])
  return (
    <Stack>
      <Group>
        <Select value={perpetual.bank.id.toString()} onChange={handleChangeBank} data={banks.map(it => ({value: it.id.toString(), label: it.denomination}))}/>
        <Text>{perpetual.bank.baseIso}</Text>
      </Group>
      <PerpetualContext.Provider value={perpetual}>
        <Outlet/>
      </PerpetualContext.Provider>
    </Stack>
  )
}

export default Admin
