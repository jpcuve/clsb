import {createContext, useEffect, useState} from 'react'
import {Bank, defaultPerpetual, Perpetual} from '../entities.ts'
import {Group, Select, Stack, Text} from '@mantine/core'
import {Outlet} from 'react-router-dom'
import secureClient from "../client.ts";
import {notifications} from "@mantine/notifications";

export const PerpetualContext = createContext<Perpetual>(defaultPerpetual)

const Admin = () => {
  const [banks, setBanks] = useState<Bank[]>([])
  const [perpetual, setPerpetual] = useState<Perpetual>(defaultPerpetual)
  const handleChangeBank = async (idAsString: string|null) => {
    if (idAsString){
      const bankId = Number(idAsString)
      const perpetual = await secureClient.perpetual(bankId)
      setPerpetual(perpetual)
    }
  }
  useEffect(() => {
    (async () => {
      try {
        const banks = await secureClient.banks()
        setBanks(banks)
        if (banks.length > 0){
          await handleChangeBank(banks[0].id.toString())
        }
      } catch (e: any) {
        console.log(e)
        if (e instanceof Error){
          notifications.show({color: 'red', message: e.message})
        }
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
