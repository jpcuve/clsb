import {Text, Button, Group, Flex, AppShell, Menu, Select} from '@mantine/core'
import {useEffect, useState} from 'react'
import {useNavigate} from 'react-router'
import {Outlet, useSearchParams} from 'react-router-dom'
import secureClient from "./client.ts";
import PerpetualContext from './contexts/PerpetualContext.ts';
import {Bank, defaultPerpetual, Perpetual} from "./entities.ts";

function App() {
  console.log(`Starting: ${import.meta.env.VITE_APP_TITLE}`)
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const [perpetual, setPerpetual] = useState<Perpetual>(defaultPerpetual)
  const [banks, setBanks] = useState<Bank[]>([])
  const handleChangeBank = async (idAsString: string|null) => {
    if (idAsString){
      const bankId = Number(idAsString)
      const perpetual = await secureClient.perpetual(bankId)
      setPerpetual(perpetual)
    }
  }
  const signInOut = () => {
    if (secureClient.signedIn){
      secureClient.signOut()
      navigate('/')
    } else {
      const search = secureClient.authorizeSearchParameters('openid email profile')
      window.location.replace(`${import.meta.env.VITE_APP_IDENTITY_URL}/sign-in?${search}`)
    }
  }
  const handleError = (message: string) => navigate(`/error?${message}`)
  useEffect(() => {
    (async () => {
      const code = searchParams.get('code')
      if (!secureClient.signedIn && code){
        try {
          await secureClient.signIn(code, searchParams.get('scope') || '')
          const banks = await secureClient.banks()
          setBanks(banks)
          if (banks.length > 0){
            await handleChangeBank(banks[0].id.toString())
          }
          navigate('admin/dashboard')
        } catch(e: any){
          handleError(e.message)
        }
      }
    })()
  }, [])
  return secureClient.signedIn ? (
    <AppShell header={{height: {base: 45}}} p="sm">
      <AppShell.Header>
        <Group justify="space-between" h="100%" p={2}>
          <Text>CLSB</Text>
          <Select value={perpetual.bank.id.toString()} onChange={handleChangeBank} data={banks.map(it => ({value: it.id.toString(), label: it.denomination}))}/>
          <Menu shadow="md">
            <Menu.Target>
              <Button variant="transparent">{secureClient.userinfo.name}</Button>
            </Menu.Target>
            <Menu.Dropdown>
              <Menu.Item onClick={signInOut}>Sign-out</Menu.Item>
            </Menu.Dropdown>
          </Menu>
        </Group>
      </AppShell.Header>
      <AppShell.Main>
        <PerpetualContext.Provider value={perpetual}>
          <Outlet/>
        </PerpetualContext.Provider>
      </AppShell.Main>
    </AppShell>
  ) : (
      <Flex justify="center" align="center" h="100vh">
        <Button onClick={signInOut}>Sign-in</Button>
      </Flex>
    )
}

export default App
