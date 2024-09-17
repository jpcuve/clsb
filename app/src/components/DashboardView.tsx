import {FC, useEffect, useState} from 'react'
import {Account, Bank, Currency} from '../entities.ts'
import {Text, Stack} from '@mantine/core'
import useClient from '../hooks/useClient.ts'

const DashboardView: FC<{bank: Bank}> = props => {
  console.log(`Dashboard for bank: ${props.bank.denomination}`)
  const client = useClient()
  const [accounts, setAccounts] = useState<Account[]>([])
  const [currencies, setCurrencies] = useState<Currency[]>([])
  useEffect(() => {
    console.log(`Excuting useEffect for bank: ${props.bank.denomination}`);
    (async () => {
      try {
        const perpetual = await client.perpetual(props.bank.id)
        setAccounts(perpetual.accounts)
        setCurrencies(perpetual.currencies)
      } catch (e: any) {
        console.error(e.message)
      }
    })()
  }, [props.bank])
  return (
    <Stack>
      <Text>Dashboard view: {props.bank.denomination}</Text>
      <Text>{JSON.stringify(accounts)}</Text>
      <Text>{JSON.stringify(currencies)}</Text>
    </Stack>
  )
}

export default DashboardView
